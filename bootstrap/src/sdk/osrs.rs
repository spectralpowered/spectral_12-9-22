#![allow(non_snake_case)]

use jni_fn::jni_fn;

pub fn init() {
    log::info!("Initializing Spectral SDK.");
}

#[jni_fn("org.spectralpowered.engine.Engine")]
pub fn unhookJagWindow(address: usize) {
    log::info!("Preparing to apply hooks at {}.", address);



    log::info!("Successfully applied hooks.");
}