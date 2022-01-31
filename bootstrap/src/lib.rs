use std::panic::catch_unwind;
use std::ptr::null;
use std::thread;
use dirs::home_dir;
use rucaja::{Jvm, JvmAttachment, JvmClass, JvmMethod};
use simple_logger::SimpleLogger;
use winapi::shared::minwindef::{DWORD, HMODULE, LPVOID};
use winapi::um::consoleapi::AllocConsole;
use winapi::um::libloaderapi::DisableThreadLibraryCalls;
use winapi::um::winnt::DLL_PROCESS_ATTACH;

#[link(name = "jvm")] extern "C" {}

fn create_console() {
    unsafe { AllocConsole(); }
    SimpleLogger::new().init().unwrap();
    log::info!("Created Spectral debug console window.");
}

fn bootstrap() {
    log::info!("Bootstrapping Spectral JVM into current process.");

    /*
     * Create JVM and load Spectral client into it's classpath.
     */
    let spectral_jar_path = home_dir().unwrap().join(".spectral\\bin\\spectral.jar");
    let classpath_option_str = format!("-Djava.class.path={}", spectral_jar_path.as_path().display());
    let jvm_options = [classpath_option_str.as_ref()];
    let jvm = Jvm::new(&jvm_options);
    let attachment = JvmAttachment::new(jvm.jvm());

    /*
     * Load the Spectral client 'Spectral.class' and invoke the static method 'start' which
     * is the Spectral client's entrypoint.
     */
    let class = JvmClass::get_class(&attachment, "org/spectralpowered/client/Spectral")
        .expect("Failed to find Spectral JVM class.");
    let method = JvmMethod::get_static_method(&attachment, &class, "start", "()V")
        .expect("Failed to find start method in Spectral.class.");
    JvmMethod::call_static_void_method(&attachment, &class, &method, null());

    log::info!("Bootstrap completed successfully.");
}


fn dll_attach() -> Result<(), ()> {
    log::info!("Initializing bootstrap.");

    /*
     * Create a debugging console window if needed while developing within an
     * IDE environment.
     */
    create_console();

    /*
     * Create a JVM within the current process and bootstrap the Spectral JVM client and
     * start it.
     */
    bootstrap();

    /*
     * Completed Spectral bootstrapper.
     */
    Ok(())
}

#[no_mangle]
#[export_name = "DllMain"]
pub extern "stdcall" fn dll_main(
    module: HMODULE,
    reason: DWORD,
    _: LPVOID
) -> i32 {
    if reason == DLL_PROCESS_ATTACH {
        unsafe {
            DisableThreadLibraryCalls(module);
        }

        thread::spawn(move || {
            match catch_unwind(dll_attach) {
                Err(_) => { log::error!("DLL injection failed!"); }
                Ok(r) => {
                    if let Some(e) = r.err() { log::error!("DLL injection failed with error: {:#?}", e); }
                }
            }
        });
    }

    true as i32
}