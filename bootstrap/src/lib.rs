use std::panic::catch_unwind;
use std::thread;
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
    lp_reserved: LPVOID
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