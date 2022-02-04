mod jvm_loader;

use std::error::Error;
use std::panic::catch_unwind;
use std::thread;
use simple_logger::SimpleLogger;
use winapi::um::consoleapi::AllocConsole;

fn open_console() {
    unsafe { AllocConsole(); }
    SimpleLogger::new().init().unwrap();
    log::info!("Opened Spectral debugging console window.");
}

fn bootstrap() -> Result<(), Box<dyn Error>> {
    open_console();

    /*
     * Load the Spectral client via JVM.
     */
    jvm_loader::run()?;

    Ok(())
}

#[mem::dll_main]
fn main() {
    thread::spawn(move || {
        match catch_unwind(bootstrap) {
            Ok(ret) => {
                if let Some(err) = ret.err() { log::error!("Failed to bootstrap Spectral. Error: {:?}", err); }
            }
            Err(err) => { log::error!("Failed to bootstrap Spectral. Error: {:?}", err); }
        }
    });
}