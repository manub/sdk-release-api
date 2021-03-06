/**
 * Copyright 2014 Marco Vermeulen
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * 	http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package net.gvmtool.release

import net.gvmtool.release.response._
import org.springframework.http.ResponseEntity

case class AccessToken(value: String)

trait Authorisation {
  implicit val accessToken: AccessToken
}

case class AuthorisationDeniedException(message: String) extends RuntimeException(message: String)

object Authorised {
  def apply(fun: => ResponseEntity[SuccessResponse])(implicit token: AccessToken, header: String) =
    if(token.value == header) fun
    else throw AuthorisationDeniedException("Invalid access token provided.")
}