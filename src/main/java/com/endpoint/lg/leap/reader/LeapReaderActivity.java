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
import interactivespaces.util.data.json.JsonBuilder;

import com.endpoint.lg.support.message.GestureMessages;

import java.util.Map;

/**
 * A LEAP event publisher activity. Only handles events when the activity is
 * activated.
 * 
 * @author Matt Vollrath <matt@endpoint.com>
 */
public class LeapReaderActivity extends BaseRoutableRosActivity {
  /**
   * The LEAP endpoint, from which events spring.
   */
  private GestureEndpoint leapEndpoint;

  /**
   * Listener for gesture events.
   */
  private GestureListener gestureListener = new GestureListener() {
    public void onGestures(Map<String, Gesture> gestures) {
      JsonBuilder msg = new JsonBuilder();
      GestureMessages.serializeGestures(gestures, msg);

      sendOutputJsonBuilder("gestures", msg);

      getLog().debug(String.format("sent gestures: %s", msg.toString()));
    }
  };

  /**
   * Listener for hand events.
   */
  private GestureHandListener handListener = new GestureHandListener() {
    public void onGestureHands(Map<String, GestureHand> hands) {
      JsonBuilder msg = new JsonBuilder();
      GestureMessages.serializeGestureHands(hands, msg);

      sendOutputJsonBuilder("hands", msg);

      getLog().debug(String.format("sent hands: %s", msg.toString()));
    }
  };

  /**
   * Listener for pointable events.
   */
  private GesturePointableListener pointableListener = new GesturePointableListener() {
    public void onGesturePointables(Map<String, GesturePointable> pointables) {
      JsonBuilder msg = new JsonBuilder();
      GestureMessages.serializeGesturePointables(pointables, msg);

      sendOutputJsonBuilder("pointables", msg);

      getLog().debug(String.format("sent pointables: %s", msg.toString()));
    }
  };

  /**
   * Starts up a LEAP endpoint.
   */
  @Override
  public void onActivitySetup() {
    LeapMotionGestureService leapSvc =
        getSpaceEnvironment().getServiceRegistry()
            .getService(LeapMotionGestureService.SERVICE_NAME);

    leapEndpoint = leapSvc.newGestureEndpoint(getLog());

    addManagedResource(leapEndpoint);
  }

  /**
   * Hooks up the messaging listeners.
   */
  @Override
  public void onActivityActivate() {
    leapEndpoint.addGestureListener(gestureListener);
    leapEndpoint.addHandListener(handListener);
    leapEndpoint.addPointableListener(pointableListener);
  }

  /**
   * Unhooks the messaging listeners.
   */
  @Override
  public void onActivityDeactivate() {
    leapEndpoint.removeGestureListener(gestureListener);
    leapEndpoint.removeHandListener(handListener);
    leapEndpoint.removePointableListener(pointableListener);
  }
}
