// The wasm-pack uses wasm-bindgen to build and generate JavaScript binding file.
// Import the wasm-bindgen crate.
use wasm_bindgen::prelude::*;

#[wasm_bindgen]
pub fn ops(a:i32, b:i32, op:String) -> i32 {
    if (op == "add") {
        return add(a, b);
    } else if (op == "sub") {
        return sub(a, b);
    }
    return add(a, b);
}

#[wasm_bindgen]
pub fn add(a:i32, b:i32) -> i32 {
    a + b
}

#[wasm_bindgen]
pub fn sub(a:i32, b:i32) -> i32 {
    a - b
}
