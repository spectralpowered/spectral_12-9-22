#![allow(non_snake_case)]
#[link(name = "jvm")] extern "C" {}

use rucaja::{Jvm, JvmAttachment, JvmClass, JvmMethod};
use simple_logger::SimpleLogger;
use winapi::ctypes::c_void;
use winapi::shared::minwindef::{BOOL, DWORD, HMODULE, LPVOID, TRUE};
use winapi::um::consoleapi::AllocConsole;
use winapi::um::libloaderapi::DisableThreadLibraryCalls;
use winapi::um::processthreadsapi::CreateThread;
use winapi::um::winnt::DLL_PROCESS_ATTACH;

fn new_console() {
    unsafe { AllocConsole(); }
    SimpleLogger::new().init().unwrap();
    log::info!("Created Spectral debug console window.");
}

fn create_jvm() {
    log::info!("Creating Spectral client JVM.");

    let jvm_options = [
        "-Djava.class.path=D:\\Libraries\\Development\\RuneScape\\projects\\organizations\\SpectralPowered\\projects\\spectral\\client\\build\\libs\\spectral-client.jar"
    ];
    let jvm = Jvm::new(&jvm_options);
    let jvm_attachment = JvmAttachment::new(jvm.jvm());

    /*
     * Invoke the Spectral client 'start' method in the JVM.
     */
    let class = JvmClass::get_class(&jvm_attachment, "org/spectralpowered/client/Spectral")
        .expect("Failed to load JVM class.");

    let method = JvmMethod::get_static_method(&jvm_attachment, &class, "start", "()V")
        .expect("Failed to load JVM method.");

    JvmMethod::call_static_void_method(&jvm_attachment, &class, &method, std::ptr::null());

    log::info!("Successfully created and invoked Spectral client JVM.");
}

unsafe extern "system" fn init(_hmodule: *mut c_void) -> u32 {
    /*
     * The 'create_console' fn should be commented out when compiling release binaries and only used
     * while in an IDE development environment.
     */
    new_console();

    /*
     * Create a new JVM with the Spectral client jar as the classpath.
     */
    create_jvm();

    0
}

#[allow(non_snake_case)]
#[no_mangle]
pub unsafe extern "system" fn DllMain(hmodule: HMODULE, dwreason: DWORD, _: LPVOID) -> BOOL {
    DisableThreadLibraryCalls(hmodule);
    if dwreason == DLL_PROCESS_ATTACH {
        CreateThread(std::ptr::null_mut(), 0, Some(init), hmodule as _, 0, std::ptr::null_mut());
    }
    TRUE
}