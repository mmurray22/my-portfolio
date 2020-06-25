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

/*
 * Displays intro paragraph
 */
function intro() {
   document.getElementById("wrapper").innerHTML = `Hi everyone! My name is
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
	school’s security club and taking violin lessons.`;
   console.log('Intro!'); 
}

/*
 * Displays tech skills
 */
function tech() { 
   document.getElementById("wrapper").innerHTML = `Rust, C++, C, JS`;
   console.log('Tech!'); 
}

/*
 * Displays interests
 */
function interests() { 
   document.getElementById("wrapper").innerHTML = `Violin, Piano, AppliedCyber`; 
   console.log('Interests!');   
}

/*
 * Naviagates to the pictures page
 */
function pictures() {
  /*More to add*/
  console.log(`Pictures!`);
}

