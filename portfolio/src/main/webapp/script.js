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
 * Navigates to the blog page
 */
function blog() {
    location.replace("/blog.html");
   console.log(`Blog`);
}

/*
 * Displays interests
 */
function interests() {
   document.getElementById("wrapper").innerHTML = `Violin, Piano, AppliedCyber`; 
   console.log('Interests!');   
}

/** Creates a map and adds it to the page. */
function loadMap() {
    var desiredLocationLatLng = {lat: 46.233950, lng: 6.055801};
    if (document.getElementById("locations").value == "Ramen") {
        desiredLocationLatLng = {lat: 37.445640, lng: -122.160736};
    }
    if (document.getElementById("locations").value == "Havanna") {
        desiredLocationLatLng = {lat: 23.115055, lng: -82.365972};
    }
    console.log(desiredLocationLatLng);
    const map = new google.maps.Map(
        document.getElementById('map'),
        {center: desiredLocationLatLng, zoom: 16
    });
    const marker = new google.maps.Marker({
        position: desiredLocationLatLng, 
        map: map,
        animation: google.maps.Animation.DROP,
        title: 'Travel Destinations'
      });
    marker.addListener('click', function() {
        if (this.getAnimation() !== null) {
          this.setAnimation(null);
        } else {
          this.setAnimation(google.maps.Animation.BOUNCE);
        }
    });
}

function getComments() {
	fetch('/data').then(response => response.json()).then((comments) => {
		console.log(comments);
		const bodyElement = document.body;
		bodyElement.innerHTML += 'Here are the comments: \n';
        for (comment of comments) {
            bodyElement.innerHTML += comment + '\n';
        }
	});
}