/*
 * Copyright (C) 2021 Spectral Powered <Kyle Escobar>
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */

package org.spectralpowered.api

enum class LoginPage(val id: Int) {
    STEAM_LOGIN(0),
    NORMAL_LOGIN(2),
    INVALID_CREDENTIALS(3),
    AUTHENTICATOR(4),
    FORGOT_PASSWORD(5),
    BIRTHDAY_NOT_SET(7),
    MOBILE_PLAY_NOW(10),
    SIGN_OUT_CONFIRMATION(11),
    MOBILE_TOS_ACCEPT_DECLINE(12),
    MOBILE_TOS_REQUIRE_ACCEPT(13),
    ACCOUNT_DISABLED(14),
    FAILED_TO_LOGIN(18),
    MOBILE_CELLULAR_USAGE_ACCEPT(21),
    TOO_MANY_LOGIN_ATTEMPTS(22),
    MOBILE_UNABLE_TO_CONNECT_GOOGLE(23),
    STEAM_LOGIN_SETUP(26),
    STEAM_DO_LOGIN(27),
    STEAM_SETUP_REQUIRED(28),
    STEAM_NOT_RUNNING(29),
    UNKNOWN(-1);

    companion object {
        fun fromId(id: Int): LoginPage = enumValues<LoginPage>().firstOrNull { it.id == id } ?: UNKNOWN
    }
}