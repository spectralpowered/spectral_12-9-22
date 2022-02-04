use std::error::Error;
use std::path::Path;
use std::ptr::null;
use dirs::home_dir;
use rucaja::{Jvm, JvmAttachment, JvmClass, JvmMethod};

pub static mut JVM: Option<Jvm> = None;
pub static mut JVM_ATTACHMENT: Option<JvmAttachment> = None;

pub fn run() -> Result<(), Box<dyn Error>> {
    /*
     * Create the Embedded JVM using the Spectral client jar inside the classpath.
     */
    let spectral_jar_path = home_dir().unwrap().join(".spectral\\bin\\spectral.jar");
    create_jvm(spectral_jar_path.as_path());

    /*
     * Invoke the Spectral JVM main() method in 'org.spectralpowered.client.Spectral'.
     */
    let spectral_class = "org/spectralpowered/client/Spectral";
    invoke_jvm(spectral_class);

    Ok(())
}

fn create_jvm(classpath: &Path) {
    log::info!("Creating JVM inside Old School RuneScape process.");

    let jvm_option_str = format!("-Djava.class.path={}", classpath.display());
    let jvm_options = [jvm_option_str.as_str()];
    let jvm = Jvm::new(&jvm_options);
    let jvm_attachment = JvmAttachment::new(jvm.jvm());

    unsafe {
        JVM = Some(jvm);
        JVM_ATTACHMENT = Some(jvm_attachment);
    }
}

fn invoke_jvm(class_name: &str) {
    log::info!("Starting Spectral client JVM.");

    unsafe {
        let class = JvmClass::get_class(JVM_ATTACHMENT.as_ref().unwrap(), class_name)
            .expect("Failed to load JVM class.");

        let method = JvmMethod::get_static_method(JVM_ATTACHMENT.as_ref().unwrap(), &class, "main", "()V")
            .expect("Failed to load JVM method from Spectral class.");

        JvmMethod::call_static_void_method(JVM_ATTACHMENT.as_ref().unwrap(), &class, &method, null());
    }
}