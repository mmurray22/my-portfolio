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

package com.google.sps.data;

import java.util.ArrayList;
import java.util.List;

/** Class containing server statistics. */
public final class MyComments {

  private final String c1;
  private final String c2;
  private final String c3;

  public MyComments(ArrayList<String> arr) {
    this.c1 = arr.get(0);
    this.c2 = arr.get(1);
    this.c3 = arr.get(2);
  }

  public String getCommentOne() {
    return c1;
  }

  public String getCommentTwo() {
    return c2;
  }

  public String getCommentThree() {
    return c3;
  }

}


