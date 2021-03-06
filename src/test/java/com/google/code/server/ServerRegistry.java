/**
 *    Copyright (C) 2011 Nadim Benabdenbi <nadim.benabdenbi@gmail.com>
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 */
package com.google.code.server;

import java.util.concurrent.ConcurrentHashMap;

/**
 * 
 * @author Nadim Benabdenbi
 * 
 */
public class ServerRegistry {
	
	private static ConcurrentHashMap<String, Server> servers = new ConcurrentHashMap<String, Server>();
	
	public static void register(String name, Server server) {
		servers.put(name, server);
	}
	
	public static Server unregister(String name) {
		return servers.remove(name);
	}
}
