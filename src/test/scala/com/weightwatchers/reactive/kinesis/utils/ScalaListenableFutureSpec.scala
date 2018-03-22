/*
 Copyright 2012-2013 Amazon.com, Inc. or its affiliates. All Rights Reserved.
 *
 * Licensed under the Amazon Software License (the "License").
 * You may not use this file except in compliance with the License.
 * A copy of the License is located at
 *
 * http://aws.amazon.com/asl/
 *
 * or in the "license" file accompanying this file. This file is distributed
 * on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either
 * express or implied. See the License for the specific language governing
 * permissions and limitations under the License.
 */

package com.weightwatchers.reactive.kinesis.utils

import com.google.common.util.concurrent.SettableFuture
import org.scalatest.concurrent.ScalaFutures
import org.scalatest.mockito.MockitoSugar
import org.scalatest.{BeforeAndAfterAll, FreeSpec, Matchers}

/**
  * Tests the implicit future conversions.
  */
class ScalaListenableFutureSpec
    extends FreeSpec
    with Matchers
    with MockitoSugar
    with BeforeAndAfterAll {

  implicit val ece = scala.concurrent.ExecutionContext.global

  "The ScalaListenableFuture" - {

    "Should convert a successful ListenableFuture to a Successful Scala Future" in {

      val f: SettableFuture[String] = SettableFuture.create()
      f.set("SUCCESS")

      import FutureUtils._

      val scalaFuture = f.asScalaFuture

      ScalaFutures.whenReady(scalaFuture) { f =>
        f should equal("SUCCESS")
      }
    }

    "Should convert a failed ListenableFuture to a failed Scala Future" in {

      val throwable                 = new Throwable
      val f: SettableFuture[String] = SettableFuture.create()
      f.setException(throwable)

      import FutureUtils._

      val scalaFuture = f.asScalaFuture

      ScalaFutures.whenReady(scalaFuture.failed) { f =>
        f should equal(throwable)
      }
    }
  }

}
