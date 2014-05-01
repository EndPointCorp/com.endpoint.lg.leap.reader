/*
 * Copyright (C) 2014 Google Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */

package com.endpoint.lg.leap.reader;

import interactivespaces.activity.impl.ros.BaseRoutableRosActivity;
import interactivespaces.service.image.gesture.leapmotion.LeapMotionGestureService;
import interactivespaces.service.image.gesture.GestureEndpoint;
import interactivespaces.service.image.gesture.Gesture;
import interactivespaces.service.image.gesture.GestureHand;
import interactivespaces.service.image.gesture.GesturePointable;
import interactivespaces.service.image.gesture.GestureListener;
import interactivespaces.service.image.gesture.GestureHandListener;
import interactivespaces.service.image.gesture.GesturePointableListener;

import java.util.Map;

/**
 * A LEAP event publisher activity.
 * 
 * @author Matt Vollrath <matt@endpoint.com>
 */
public class LeapReaderActivity extends BaseRoutableRosActivity {
  @Override
  public void onActivitySetup() {
    LeapMotionGestureService leapSvc =
        getSpaceEnvironment().getServiceRegistry()
            .getService(LeapMotionGestureService.SERVICE_NAME);

    GestureEndpoint leapEndpoint = leapSvc.newGestureEndpoint(getLog());

    leapEndpoint.addGestureListener(new GestureListener() {
      public void onGestures(Map<String, Gesture> gestures) {
        getLog().info(String.format("got gestures: %s", gestures.toString()));
      }
    });

    leapEndpoint.addHandListener(new GestureHandListener() {
      public void onGestureHands(Map<String, GestureHand> hands) {
        getLog().info(String.format("got hands: %s", hands.toString()));
      }
    });

    leapEndpoint.addPointableListener(new GesturePointableListener() {
      public void onGesturePointables(Map<String, GesturePointable> pointables) {
        getLog().info(String.format("got pointables: %s", pointables.toString()));
      }
    });

    addManagedResource(leapEndpoint);
  }
}
