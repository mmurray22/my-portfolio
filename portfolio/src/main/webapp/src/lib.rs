// The wasm-pack uses wasm-bindgen to build and generate JavaScript binding file.
// Import the wasm-bindgen crate.
use wasm_bindgen::prelude::*;

// Our Add function
// wasm-pack requires "exported" functions
// to include #[wasm_bindgen]
#[wasm_bindgen]
pub fn intro(name: String) -> String {
    let result = format!("{} {}", "Hello World and Hello", input_string);
    return result.into();
}

pub fn add(a: i32, b: i32) -> i32 {
  return a + b;
}

pub fn sub(a:i32, b:i32) -> i32 {
  return a - b;
}

