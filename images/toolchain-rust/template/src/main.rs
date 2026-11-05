//! The starting project of a Rust exercise: a program, with its tests.

fn greeting(name: &str) -> String {
    format!("Ciao, {name}!")
}

fn main() {
    println!("{}", greeting("mondo"));
}

#[cfg(test)]
mod tests {
    use super::*;

    #[test]
    fn greets_by_name() {
        assert_eq!(greeting("Ada"), "Ciao, Ada!");
    }
}
