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

var $profile = $('#profile'),
    lFollowX = 0,
    lFollowY = 0,
    lFollowZ = 0,
    x = 0,
    y = 0,
    friction = 1 / 10;

$(document).ready(function() {
  function animate() {
    x += (lFollowX - x) * friction;
    y += (lFollowY - y) * friction;
    $('#profile').css({
        'transform': 'perspective(600px) rotateY(' + -x + 'deg) rotateX(' + y + 'deg)'
    });
    window.requestAnimationFrame(animate);
  }

  $('#profile').on('mousemove click', function(e) {
        var lMouseX = Math.max(-100, Math.min(100, $('#profile').width()*2 - e.clientX));
        var lMouseY = Math.max(-100, Math.min(100, $('#profile').height() / 2 - e.clientY));
        lFollowX = (12 * lMouseX) / 100; // 100 : 12 = lMouxeX : lFollow
        lFollowY = (10 * lMouseY) / 100;
        /*Animate!*/
        animate();
  });
});

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
   document.getElementById("wrapper").innerHTML = `My favorite languages include: Rust, C++, C.`;
   console.log('Tech!'); 
}

/*
 * Displays interests
 */
function interests() { 
   document.getElementById("wrapper").innerHTML = `My hobbies outside of classwork and research include Violin, Piano and AppliedCyber.
                                                    I have played Violin and Piano since I was 4 and 3 years old respectively. Music is one of 
                                                    my deepest passions outside of computing, and I am very happy to continue taking private 
                                                    lessons at my university!
                                                    AppliedCyber is a club centered around security, analyzing it from a technical, political,
                                                    and social lense. One of my favorite parts of AppliedCyber are the competitions we participate
                                                    in, where we get to travel across the United States and meet many people heavily 
                                                    involved in the security scene.`; 
   console.log('Interests!');   
}

/*
 * Navigates to the blog page
 */
function blog() {
   location.replace("/blog.html");
   console.log(`Blog`);
}

/*
 * Navigates to the landing page
 */
function return_home() {
   location.replace("/index.html");
   console.log(`Return Home!`);
}

/*
 * Naviagates to the pictures page
 */
function pictures() {
  document.getElementById("wrapper").innerHTML = `<img id=\"profile\" src=\"/images/PROFILE_PIC.JPG\">`;
  console.log(`Pictures!`);
}

