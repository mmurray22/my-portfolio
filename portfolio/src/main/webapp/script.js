// Copyright 2019 Google LLC
//
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at
//
//     https://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and
// limitations under the License.

function intro() {
   document.getElementById("para-one").innerHTML = `Hi everyone! My name is
	Michaela Murray, and I’m currently a rising junior at Stanford
	University.  I am majoring in Computer Science with a concentration in
	Systems, and I plan to minor in mathematics and music. So far, my
	favorite CS class is Operating Systems, which was taught in C. I’ve also
	enjoyed my Networking class taught in C++ and a distributed systems
	paper-reading class where I did the final project in Rust. Last summer,
	I did research in video analytics, and throughout this past year I have
	continued to engage in research, the most of which was developing a
	sensor peripheral driver for TockOS.  When I am not taking classes or
	doing research, my main activities are being Vice President of our
	school’s security club and taking violin lessons. A fun fact about me is
	that I am 5’, which you might not be able to tell over GVC.`;
   console.log('Intro!'); 
}

function tech() { 
   document.getElementById("para-one").innerHTML = `Rust, C++, C, JS`;
   console.log('Tech!'); 
}

function interests() { 
   document.getElementById("para-one").innerHTML = `Violin, Piano, AppliedCyber`; 
   console.log('Interests!');   
}

function blog() {
   location.replace("/blog.html");
   console.log(`Blog`);
}

function contact() { 
   document.getElementById("para-two").style.display = "block";
   console.log('Contact!'); 
}

function background() {
   let today = new Date();
   if (today.getHours() <= 8 || today.getHours() >= 19) { //Nighttime
	document.body.style.background = 
		   `linear-gradient(to top, #a8edea 0%, #fed6e3 100%) 
	   	    no-repeat center center fixed`;
   } else if (today.getHours() > 8 && today.getHours() <= 16) { //Daytime
	document.body.style.background = 
		   `linear-gradient(to right, #ffecd2 0%, #fcb69f 100%) 
	 	    no-repeat center center fixed`;
   } else if (today.getHours() > 16 && today.getHours() <= 18) { //Evening
	document.body.style.background =  
		   `linear-gradient(to top, #30cfd0 0%, #330867 100%) 
	            no-repeat center center fixed`;
   }
   console.log(`Background`);
}

/*
 * TODO: FINISH PARALLAX
 */
function mouseparallax(image) {
  let followX, followY, x, y = 0;
  let friction = 1/10;
  i
  console.log(`Mouse parallax`); 
}
