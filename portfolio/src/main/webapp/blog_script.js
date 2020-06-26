/*
 * This is the JavaScript file for 
 * the blog portion of the website
 */
import init from "./pkg/portfolio.js";

function return_home() {
   location.replace("/index.html");
   console.log(`Return Home!`);
}

const wasmRandNum = async() => {
   const randNum = await init("./pkg/portfolio_bg.wasm");
   document.getElementById("val").innerHTML += randNum.num(20, 20, "add");//randNum.random_number();
}

wasmRandNum();
/*function demo_one() {
    runWasmOne();
    console.log(`Run Wasm One!`);
}

const runWasmOne = async() => {
   const helloWorld = await init("./pkg/portfolio_bg.wasm");
   console.log(helloWorld.intro("Michaela"));
   document.getElementById('demo-1').innerHTML = helloWorld.intro("Michaela");
};

runWasmOne();*/

