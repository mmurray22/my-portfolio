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

package com.google.sps;
import com.google.common.collect.Sets;

import java.util.Collection;

public final class FindMeetingQuery {
  public Collection<TimeRange> query(Collection<Event> events, MeetingRequest request) {
    Collection<TimeRange> validCalendarTimes = new ArrayList<>();
    /*Find the time ranges everyone else is not available*/
    // Collection<TimeRange> forbiddenTimes = new ArrayList<>();
    // for (Event event : events) {
    //     if ((Sets.intersection(event.getAttendees(), request.getAttendees())).size() == 0) {
    //         continue;
    //     }
    //     forbiddenTimes.add(event.getWhen());
    // }
    if (events.size() == 0) {
        validCalendarTimes.add(TimeRange.WHOLE_DAY);
        return validCalendarTimes;
    }
    Collections.sort(events, TimeRange.ORDER_BY_START);
    int start = TimeRange.START_OF_DAY;
    for (int i = 0; i < events.size(); i++) {
        if (start == events[i].getWhen().start()) { /*Time is occupied*/
            if ((i+1) != events.size() &&
                events[i].getWhen().end() > events[i+1].getWhen().start() &&
                events[i].getWhen().end() < events[i+1].getWhen().end()) {
                    start = events[i+1].getWhen().end();
                    continue;
            }
            start = events[i].getWhen().end();
            continue;
        } else { /*Time is not occupied*/
            TimeRange timeRange = new TimeRange(start, events[i+1].getWhen().start());
            if (timeRange.duration() >= request.getDuration()) {
                validCalendarTimes.add(timeRange);
            }
            start = events[i].getWhen().start();
            i--;
        }
    }
    /*Remove slices that are too small ^^maybe can do above?*/
  }
}
