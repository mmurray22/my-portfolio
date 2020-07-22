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
// import com.google.common.collect.Sets;

import java.util.Arrays;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public final class FindMeetingQuery {
  public Collection<TimeRange> query(Collection<Event> events, MeetingRequest request) {
    if (events.size() == 0) {
        return Array.asList(TimeRange.WHOLE_DAY);
    }
    if (request.getDuration() > TimeRange.WHOLE_DAY) { 
        return Array.asList();
    }
    List<Event> eventsList = sortEventsByTime(events);
    removeExtraneousEvents(eventsList, request.getAttendees());
    return bestMeetingTimes(eventsList, request.getDuration());
  }

  private List<Event> sortEventsByTime(Collection<Event> events) {
      //sort :) 
  }

  private void removeExtraneousEvents(List<Event> events, Collection<String> attendees) {
      for (Event event : events) {
        if (Set.intersection(attendees, event.getAttendees()).isEmpty()) {
            events.remove(event);
        }
      }
      return events;
  }

  private Collection<TimeRange> bestMeetingTimes(List<Event> eventsList, long duration) {
    int start = TimeRange.START_OF_DAY;
    for (int i = 0; i < events.size(); i++) {
        if (start == events.get(i).getWhen().start()) { /*Time is occupied*/
            if ((i+1) != events.size() &&
                events.get(i).getWhen().end() > events.get(i+1).getWhen().start() &&
                events.get(i).getWhen().end() < events.get(i+1).getWhen().end()) {
                    start = events.get(i+1).getWhen().end();
                    continue;
            }
            start = events.get(i).getWhen().end();
            continue;
        } else { /*Time is not occupied*/
            TimeRange timeRange = new TimeRange(start, events.get(i+1).getWhen().start());
            if (timeRange.duration() >= request.getDuration()) {
                validCalendarTimes.add(timeRange);
            }
            start = events.get(i).getWhen().start();
            i--;
        }
    }
  }
}


