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
 * This feature is largely based off this codepen: https://codepen.io/jjgodcom/details/QMxwKq/
 * Other codepens which were used to get the code to its current state:
 * https://codepen.io/hesham-farag/pen/yXzqNX
 *
 */
/*Global variables pertaining to parallax animation*/
/*var lFollowX = 0,
    lFollowY = 0,
    lFollowZ = 0,
    x = 0,
    y = 0,
    friction = 1 / 10;

$(document).ready(function() {
  function animate() {
    
    //Gets rotation values x and y
    x += (lFollowX - x) * friction;
    y += (lFollowY - y) * friction;
    $('#profile').css({
        'transform': 'perspective(600px) rotateY(' + -x + 'deg) rotateX(' + y + 'deg)'
    });
    window.requestAnimationFrame(animate);
  }
  
  $("#profile").on('mousemove click', function(e) {
        var lMouseX = Math.max(-100, Math.min(100, $('#profile').width()*2 - e.clientX));
        var lMouseY = Math.max(-100, Math.min(100, $('#profile').height()/2 - e.clientY));
        lFollowX = (12 * lMouseX) / 100;
        lFollowY = (10 * lMouseY) / 100;
        animate();
  });
});*/

function intro() { 
	document.getElementById("inner-content").style.display = "block"; 
	document.getElementById("my-info").innerHTML = `Hi everyone! My name is
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
	document.getElementById("inner-content").style.display = "block"; 
	document.getElementById("my -info").innerHTML = `Rust, C++, C, JS`;
	console.log('Tech!'); 
}

function interests() { 
	document.getElementById("inner-content").style.display = "block"; 
	document.getElementById("my-info").innerHTML = `Violin, Piano, 
						     AppliedCyber`; 
	console.log('Interests!');   
}

function contact() { 
	document.getElementById("inner-content").style.display = "block"; 
	document.getElementById("my-info").innerHTML = 
		`<ul style="list-style-type:none;"> 
		<li>Email: 
		<a href="mailto:murray22@stanford.edu"> 
		murray22@stanford.edu 
		</a></li>
		<li>Linkedin:
		<a href="https://www.linkedin.com/in/murray22/" target="_blank">
		My LinkedIn
		</a></li> 
		<li>Github: 
		<a href="https://github.com/mmurray22" target="_blank">
		My Github
		</a></li> 
		</ul>`; 
	console.log('Contact!'); 
}
