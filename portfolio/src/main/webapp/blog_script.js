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

