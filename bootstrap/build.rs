use std::path::{PathBuf};
use std::process::Command;

fn main() {
    let java_home = find_java_home().expect(
        "Failed to find Java home directory on current system."
    );
    println!("cargo:rustc-link-search=native={}", java_home.join("lib").display());
}

fn find_java_home() -> Option<PathBuf> {
    Command::new("java")
        .arg("-XshowSettings:properties")
        .arg("-version")
        .output()
        .ok()
        .and_then(|output| {
            let stdout = String::from_utf8_lossy(&output.stdout);
            let stderr = String::from_utf8_lossy(&output.stderr);
            for line in stdout.lines().chain(stderr.lines()) {
                if line.contains("java.home") {
                    let pos = line.find("=").unwrap() + 1;
                    let path = line[pos..].trim();
                    return Some(PathBuf::from(path));
                }
            }
            None
        })
}