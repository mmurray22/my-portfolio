/*
 * This is the JavaScript file for 
 * the blog portion of the website
 */
import init from "./pkg/random_fxn.js";

function return_home() {
   location.replace("/index.html");
   console.log(`Return Home!`);
}

const runWasmOne = async() => {
   const helloWorld = await init("./pkg/hello_world_bg.wasm");
   document.getElementById('#demo-1').textContent = helloWorld.intro(d);
}

function demoOne () {
    runWasmOne();
    console.log(`Run Wasm One!`);
}


