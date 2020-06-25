/*
 * This is the JavaScript file for 
 * the blog portion of the website
 */
import init from "./pkg/random_fxn.js";

function return_home() {
   location.replace("/index.html");
   console.log(`Return Home!`);
}

const randomWasm = async() => {
   const randomFxn = await init("./pkg/random_fxn_bg.wasm");
   
   document.body.textContent = randomFxn.poly_pic();
}
function random_wasm() {
   
   console.log(`Random WASM Functions!`);
}

