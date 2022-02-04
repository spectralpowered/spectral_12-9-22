use std::path::{PathBuf};
use dirs::home_dir;

fn main() {
    let jvm_lib_path = get_spectral_jre_path()
        .ok_or_else(|| format!("Failed to link Spectral JRE 'jre/lib/jvm.lib'."));
    println!("cargo:rustc-link-search=native={}", jvm_lib_path.unwrap().display());
}

fn get_spectral_jre_path() -> Option<PathBuf> {
    return Some(home_dir()?.join(".spectral\\jre\\lib"));
}

