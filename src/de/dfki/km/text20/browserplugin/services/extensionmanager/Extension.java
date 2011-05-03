/*
 * Extension.java
 * 
 * Copyright (c) 2010, Ralf Biedert, DFKI. All rights reserved.
 * 
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 2.1 of the License, or (at your option) any later version.
 * 
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 * 
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, 
 * MA 02110-1301  USA
 *
 */
package de.dfki.km.text20.browserplugin.services.extensionmanager;

import net.xeoh.plugins.base.Plugin;
import net.xeoh.plugins.base.annotations.injections.InjectPlugin;
import net.xeoh.plugins.informationbroker.InformationBroker;
import net.xeoh.plugins.informationbroker.InformationItem;
import de.dfki.km.text20.browserplugin.browser.browserplugin.BrowserAPI;
import de.dfki.km.text20.browserplugin.services.extensionmanager.annotations.ExtensionMethod;

/**
 * A {@link Plugin} (see <a href="http://jspf.xeoh.net">JSPF</a> implementing 
 * this interface can be loaded as an extension. Extensions can be used in 
 * HTML applications. Extensions can easily access internal functionality and run code 
 * that would otherwise be restricted by the JavaScript sandbox.<br/><br/>
 * 
 * There are two ways for extensions to access the remaining Text 2.0 Framework 
 * functionality. The first way is to use {@link InjectPlugin} to inject required 
 * plugins (again, see JSPF documentation). The second way is to use the 
 * {@link InformationBroker} (which, in turn, must also be injected first) and then
 * listen for objects / interfaces you are interested in (see the {@link BrowserAPI} 
 * documentation for a list of useful {@link InformationItem}s).<br/><br/>
 * 
 * Internally extensions are loaded by the {@link ExtensionManager}. The manager scans
 * each extension and searches for methods annotated by @{@link ExtensionMethod}. These
 * are then automatically exported.<br/><br/>
 * 
 * Extensions should be exported as JARs and specified in the HTML appliction 
 * configuration (see the <a href="http://code.google.com/p/text20/wiki/UsageGuide">usage guide</a>).  
 * 
 * @author Ralf Biedert
 * @since 1.4
 */
public interface Extension extends Plugin {
}
