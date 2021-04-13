/*
    ACAB (All Chats Are Beautiful)
    Copyright (C) 2021  Zeno Berkhan, Nico Diefenbacher

    This program is free software: you can redistribute it and/or modify
    it under the terms of the GNU General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    (at your option) any later version.

    This program is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU General Public License for more details.

    You should have received a copy of the GNU General Public License
    along with this program.  If not, see <https://www.gnu.org/licenses/>.
*/

package de.ndfnb.acab.data;


public class Message {
    private final String message;
    private String displayedText;
    private final int secVisible;

    public Message(String message, int secVisible) {
        this.displayedText = secVisible + " seconds available";
        this.message = message;
        this.secVisible = secVisible;
    }

    public String getMessage() {
        return message;
    }

    public int getSecVisible() {
        return secVisible;
    }

    public String getDisplayedText() {
        return displayedText;
    }

    public void open() {
        this.displayedText = this.message;
    }
}