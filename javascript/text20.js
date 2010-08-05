/*
 * text20.js
 * 
 * Copyright (c) 2010, Ralf Biedert, German Research Center For Artificial Intelligence.
 * 
 * All rights reserved.
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
 * 
 * Version:
 *      4.a.0
 * 
 * Dependencies:
 *      jquery > 1.4
 * 
 */

(function(window, undefined) {
    

var text20 = {},


    strings = {
        /** Segment a string into chunks of strings based on punctiation. */
        segment: function(text) {
            var rval = [], 
                w = "", 
                nw = ""
    
            for(var i=0; i<text.length; i++) {
                var c = text.charAt(i);
                if(c == '.' || c == ' ' || c == '\n' || c == '\t' || c == '!' || c == '#'          // FIXME: Beautify me into array 
                    || c == '\'' || c == '"' || c == '?' || c == '-' || c == ';' || c == ',' 
                    || c == '+' || c == '&' || c == ':' || c == '(' || c == ')' || c == '[' 
                    || c == ']' || c == '{' || c == '}' || c == "%") {
                    nw += c;
                    if(w.length > 0) {                  
                        rval.push(w);
                        w = "";
                    }
                } else {
                    w += c;
                    if(nw.length > 0) {
                        nw.isNW = true;
                        rval.push(nw);
                        nw = "";
                    }
                }           
            }
    
            // If there are remainders, add them as well 
            if(w.length > 0) {
                rval.push(w);
                w = "";
            }
    
            if(nw.length > 0) {
                nw.isNW = true;
                rval.push(nw);
                nw = "";
            }
            
            // Return result
            return rval;
        }		
    },
    
    
    
    math = {
        /** Generate a float random number between min and max. */
        random: function(min, max) {
            return (min + parseInt(Math.random() * (max - min + 1)));
        }
    },
    

    listener = {
        /** Creates a listener manager */
        manager: function() {
            var mgr = {},
                listener = {}
            
            /** Add a new listener to the given channel */
            mgr.add = function(channel, fnc){
                // Create channel if it's not there 
                if (!listener[channel]) { listener[channel] = [] }
                
                listener[channel].push(fnc);
            }
            
            /** Get listeners for the given chanel */
            mgr.get = function(channel){
                if (!listener[channel]) { return []; }
                
                return listener[channel];
            }
            
            /** Process all listeners for the given channel */
            mgr.process = function(channel, fct){
                this.get(channel).forEach(fct);
            }
            
            // Return the manager
            return mgr;
        },
    },
    
    
    
    callbacks = {
        /** Returns the currently used prefix */
        prefix: function() {
            return "xclback2482"
        },

        /** Returns the wrapped name for a method name */
        name: function(suffix) {
            return this.prefix() + suffix;
        },
        
        /** Returns the function object */
        func: function(name) {
           return eval(this.name(name));    
        },
        
        /** Registes a given function globally */
        register: function(name, fnc) {
            eval("window." + this.name(name) + " = fnc;");
        }
    },
    
    
    
    file = {
        /** Returns the absolute path to a relative path */
        absolutePath: function(relativePath) {
            return location.href.substring(0, location.href.lastIndexOf('/') + 1 ) + relativePath;
        }	
    },
    
    
    
    cache = {
        /** Creates a new cache. */
        cache: function() {
            var cache = {}, 
                data = {};
            
            /** Creates an element for the given id */
            cache.create = function(id){
                var cacheElement = new Object();
                data[id] = cacheElement;
                return data[id];
            }

            /** Returns an element for the given ID */		    
            cache.get = function(id){
                return data[id];
            }
            
            // Return created cache.
            return cache
        }
    },
    
    
    system = {
        /** Returns the operating system */
        os: function() {
            var OSName = null;
            if (navigator.appVersion.indexOf("Win")!=-1) OSName="Windows";
            if (navigator.appVersion.indexOf("Mac")!=-1) OSName="MacOS";
            if (navigator.appVersion.indexOf("X11")!=-1) OSName="Unix";
            if (navigator.appVersion.indexOf("Linux")!=-1) OSName="Linux";
            return OSName;			
        }		
    },
    
    
    
    browser = {
        /** Use this to manually override the offset */
        overrideOffset: null,
        
        /** Internally used for calibrated offsets */
        calibratedOffset: null,
        
        /** Returns a unique ID for this browser */
        id: function() {
            return navigator.appVersion; // TODO: Improve this
        },
        
        /** Returns the offset of the document from the window's screen area */
        documentOffset: function() {
            // If an offset is present, use it
            if(this.overrideOffset) {
                return this.overrideOffset;
            }
            
            // FIXME: Determine automatically
            if(system.os() == "Windows")
                return [5, 84];
            else if(system.os() == "MacOS")
                return [0, 57];
        }
    },
    
    
    
    dom = {
        
        /** Creates an unique id */
        id: function() { 
            if(!this.lastID) this.lastID = 0
            return "t20id" + this.lastID++;
        },
    
                
        /**
         * Ensures the element has an ID. If it has, the ID is returned. If it doesn't, 
         * a new ID is created and it is returned.
         *
         * @param {Object} element to IDify
         *
         * @returns: The ID created
         */
        ensureID: function(element) {
            var e = $(element);
            
            // If it has an ID return that ...       
            if(e.attr("id")) return e.attr("id");
            
            // ... else, add autoID
            var id = dom.id();
            e.attr("id", id);
            e.addClass("autoID");
            return id;
        },       
        
        
        /** Finds elements in a region */ 
        elementsAround: function(x, y, xradius, yradius) {  
            var rval = []
            
            // FIXME: Better alternative to 'shooting'?
            for(var ypos = y - yradius; ypos < y + yradius; ypos += 10) {
                for(var xpos = x - xradius; xpos < x + xradius; xpos += 10) {
                    
                    // FIXME: This is broker (Safari 5 uses coordinats differently?)
                    //var element = document.elementFromPoint(xpos, ypos);
                    
                    if(!element) continue;
                    if(rval.indexOf(element) >= 0) continue;
                                 
                    rval.push(element);             
                }
            }
                    
            return rval;
        },
          
              
        /** Returns all parents above a given element, including the element */
        parents: function(element) {
            var rval = []
    
            if(element==null) return rval;
            rval.push(element)
    
            while(element.parentNode){     
                rval.push(element.parentNode)
                element = element.parentNode
            }
    
            return rval
        },  
        
                   
        /**
         * Finds the position of an element relative to the web page
         * 
         * @param {Object} obj
         *
         * @returns: The position as an [x, y, relativeTo] array.
         */
        documentPosition: function(obj){
            // Original Object
            var origObj = obj;
            
            // Will be set below, position of the element relativ to document start
            var posX = obj.offsetLeft;
            var posY = obj.offsetTop;
        
            var relativeTo = "document";
       
            var scrollCorrectionY = 0;  
                    
            // Check all parent nodes if element is fixed to window or normal inside the document ...
            while(obj.parentNode){    
                    var type = window.getComputedStyle(obj,"").getPropertyValue("position");
                    if(type=="fixed") {
                        relativeTo = "window"
                    }    
                    
                    // Subtract scrolling of parent (some divs might have this ... BUT IGNORE THE BODY)
                    if (obj.parentNode.scrollTop > 0 && obj.parentNode != document.body) {
                        scrollCorrectionY -= obj.parentNode.scrollTop;
                    }
                                                
                    obj=obj.parentNode;          
            }
            
            // Restore original object
            obj = origObj;
    
            // Obtain the screen position ...
            while(obj.offsetParent){     
                posX=posX+obj.offsetParent.offsetLeft;
                posY=posY+obj.offsetParent.offsetTop;
                                    
                obj=obj.offsetParent;         
            }
    
            if(!posX) return null;
            if(!posY) return null;  
            
            // Correct scrolling 
            posY += scrollCorrectionY;
                
            return [posX, posY, relativeTo];    
        },   
        
        /**
         * Returns all text nodes below a given element
         * 
         * @param {Object} startNode
         *
         * @returns: All text nodes
         */
         textnodesBelow: function(startNode) {
            if(!startNode) return []
    
            // TODO: Replace this with powerful selector?
            var rval = [],
                length = startNode.childNodes.length;
    
            for(var i=0; i<length; i++) {
                var child = startNode.childNodes[i];
        
                // For every node, either call this function again, or 
                // put the text node into the rval
                if(child.nodeName == "#text") { 
                    // If we don't do these checks, spans will be included into Tables, resulting in a messed rendering ...
                    if(child.data.length > 0                
                        && startNode.nodeName != "TR" 
                        && startNode.nodeName != "TABLE" 
                        && startNode.nodeName != "HEAD" 
                    ) {             
                        rval.push(child);
                    }
                }
                
                // Recursively descend ...
                if(child.childNodes.length > 0) {
                    var subcall = dom.textnodesBelow(child);
                    rval = rval.concat(subcall);
                } 
            }
            
            // Return merged results
            return rval;
        },
            
        
        /**
         * Split text nodes into words and make spans out of them
         *
         * @returns: Nothing
         */
        spanify: function(arrayOfTextnodes, spanCallback){
            if(!this.lastText) this.lastText = 0
            
            var allTokens = [],
                textID = this.lastText++,    // Uniquely assign a text id
                wordID = 0,                  // And prepare word IDs
                eei = dom.ensureID;        
            
            // Process every text node we got
            arrayOfTextnodes.forEach(function(node){
                var parentNode = node.parentNode;
                var container = document.createElement("span");
                
                // If parent node already has proper attribute, don't do anything
                if (parentNode.hasAttribute("_markedText")) 
                    return;
                    
                // Replace the given node with one of our span containers 
                parentNode.replaceChild(container, node);
                
                // Split this text node
                var allText = node.data,
                    words = strings.segment(allText);
                
                // Process every single word
                words.forEach(function(token){
                    // In case this this non-word, just add it.
                    if (token.isNW) {
                        container.appendChild(document.createTextNode(token));
                    }
                    
                    // In case this is a proper word, spanify  it
                    else {
                        
                        // Create textnode and span for every word
                        var text = document.createTextNode(token),
                            newSpan = document.createElement("span");
                        
                        newSpan.setAttribute("_markedText", "true");
                        newSpan.setAttribute("_textID", textID);
                        newSpan.setAttribute("_wordID", wordID++);
    
                        // The words have an ID
                        var id = eei(newSpan);
                        
                        // Callback the listener if we have one
                        if(spanCallback) spanCallback(newSpan, id, token);
                        
                        // Append them              
                        newSpan.appendChild(text);
                        container.appendChild(newSpan);
                        
                        // Store child
                        allTokens.push(newSpan);
                    }
                });
            });
            
            return allTokens;
        },
    },
    
    
    
    connector = {
        config: {
            /** Archive to use */
            archive: "text20.jar",

            /** Should we present a load indicator? */			
            loadIndicator : false,
            
            /** Devices and locations */          
            trackingDevice: "eyetrackingdevice:auto",
            trackingURL : "discover://nearest",
            
            /** Brain Tracking device enabled*/
            enableBrainTracker: false,
            brainTrackingURL: "discover://any",

            /** 3rd Party extensions */          
            extensions : [],
            
            /** 3rd Party extensions */          			
            recordingEnabled : false,
            sessionPath : "/tmp/sessions",			
            logging : "default",
            
            /** Internal Variables */
            transmitMode : "ASYNC",    // DO NOT TOUCH THIS
        },
        
        variables: {
            /** Listeners for all our channels */
            listeners: listener.manager(),            
        },
        
        
        /** Will be set upon core.init() */
        connection: null,
        
        
        extensions: {
            
            //
            // Extension will be added in here dynamically
            // 
            
            registry: {
                /** All extension listeners */
                listeners: listener.manager(),
                
                /** Wrapped call to a registered extension */ 
                wrapper: function(name, args) {
                    
                    var first = true,
                        cmd = name + "(",
                        i = 0;
                        
                    // Assemble call ...						
                    for(i = 0; i<args.length; i++) {
                        var elem = args[i]
                        // split arguments
                        if(!first) { cmd += "," }
                        else { first = false }
                        
                        cmd += "'" + encodeURIComponent(elem) + "'"         
                        
                    }
                    cmd += ");"
                    
                    // Call and store the return value
                    var rval = connector.connection.callGeneric(cmd);
                    
                    // Convert variables. Some Java/JavaScript implementations don't do it properly ...
                    if (rval) {
                        // Fix strings ... 
                        if(rval.getClass && rval.getClass().getName() == "java.lang.String") {
                            // Check again that we have a to string method
                            if(rval.toString) {
                                rval = "" + rval.toString();    
                            } else {
                                rval = "" + rval;
                            }                
                        }
                    } 
                    
                    return rval;    
                },
                
                /** Registers a special name for latter recognition */
                register: function(name) {
                    eval("text20.connector.extensions." + name + " = function() {" 
                        +      "return text20.connector.extensions.registry.wrapper('" + name + "', arguments);"
                        + "}"); 
                },
                
                /** Adds a listener for the given extension callback channel */
                listener: function(channel, l) {
                    listeners.add(channel, l);  
                },
    
                /** Called back by the connector and dispatches calls to our registered elements */
                callback: function(name, args) {
                    var l = listeners.get(name),
                        opts = "";
                    
                    // Assemble arguments      
                    for(var i=0; i<args.length; i++) {
                        opts += ",args[" + i + "]"
                    }
                           
                    // Remove first comma
                    if(opts.length > 0) { opts = opts.substr(1) }
                            
                    // Call all listener
                    l.forEach(function f(elem) {
                        eval("elem(" + opts + ");")
                    });    
                }               
            },
        },

   
        /** Adds a listener to a given channel */
        listener: function(channel, fct){
            var supportedChannels = ["INITIALIZED", "reducedApplicationGaze", "fixation", "specialCallback", "perusal", "headPosition", "weakSaccade"];
            
            // Safety check
            if (supportedChannels.indexOf(channel) < 0) {
                alert("ERROR. Trying to register to unknown channel " + channel);
                return;
            }
            
            // Finally register
            this.variables.listeners.add(channel, fct);
        },

        
        /** How this connector can connect */
        methods: {
            dummy: {
                /** All of these methods should be considered semi private, thre should not be any need 
                    to call them from outside */ 
                connect: function() {},
                registerCallback: function(name, suffix) {},
                callGeneric: function(cmd){},
                connect: function() {},
                transmitWindowVisibility: function(isVisible) {},
                transmitElementRemoved: function(id) {},
                transmitElementPositionAnchor: function(id, anchor) {},
                transmitBrowserGeometry: function(x, y, w, h) {},
                preference: function(key, value) {},
                transmitElement: function(id, type, content, x, y, w, h) {},
                transmitViewport: function(x, y) {},
                transmitElementMetaInformation: function(id, key, value) { },
                setSessionParameter: function(key, value){},
                dropBrowserCalibration: function() {},
            },
            
            applet: {
                /** Locally used variables */
                variables: {
                    initialized: false,                 // True if the connection has been initialized 
                    engine: null,                       // Engine
                    transmitCache: cache.cache(),       // Cache to check if we need to retransmit
                    appletID: "m8doaaas33a",            // ID of our applet
                    offset: null,                       // Browser offset to use
                    batch: null,                        // Batch call for bulk transmission
                    loadIndicator: this.loadIndicator,  // TODO: What does this do?                     
                },
                                
                /** Creates a new batch for submission to the applet */
                batch: function() {
                    
                    var batch = {},                    
                        browserFlag = [],
                        elementGeometry = [],
                        elementMeta = []
                    
                    /** Update an element flag */
                    batch.updateElementFlag = function(id, type, flag){
                        browserFlag.push({ id: id, type: type, flag: flag});
                    }
                        
                    /** Update an element meta info */
                    batch.updateElementMeta = function(id, key, value){
                        elementMeta.push({ id: id, key: key, value: value});
                    }
                 
                    /** Update an element geometry info */				 
                    batch.updateElementGeometry = function(id, type, content, x, y, w, h){
                        elementGeometry.push({id:id, type:type, content:content, x:x, y:y, w:w, h:h});
                    }
                    
                    /** Generate a batch call */          					
                    batch.generateBatchCalls = function(){
                        var rval = []

                        // TODO: Generalize calls with single method
                        
                        var assembler = function(prefix, array, keyset) {
                            
                            // Construct proper prefix 
                            var str = prefix + "(",
                                opts = ""
                                                    
                            array.forEach(function(e){
                                // Iterate over keyset (we have to use a counter
                                // because the substr() version was slow as hell
                                for(var i=0; i<keyset.length; i++) {
                                    var f = keyset[i]
                                    var v = e[f] == null ? ".null" : e[f]
                                    
                                    if(f == "type" || f == "content") {
                                        v = encodeURIComponent(v)
                                    }
                                    
                                    // Concat elements with ',', last one with ';'. 
                                    if(i < keyset.length - 1)
                                        opts += v + ","
                                    else 
                                        opts += v + ";"
                                }                                
                            })
                                        
                            // Strip last semicolon                                     
                            if (opts.length > 0) 
                                opts = opts.substr(0, opts.length - 1);
                            
                            str += opts + ")";
                            rval.push(str)	
                        }  
                        
                        // Element flags
                        if (browserFlag && browserFlag.length > 0) {
                            assembler("updateElementFlag", browserFlag, ["id", "type", "flag"])		                 
                        }
                        
                        
                        // Element meta 
                        if (elementMeta && elementMeta.length > 0) {
                            assembler("updateElementMeta", elementMeta, ["id", "key", "value"])   
                        }
                                
                        // Element geometry
                        if (elementGeometry && elementGeometry.length > 0) {
                            assembler("updateElementGeometry", elementGeometry, ["id", "type", "content", "x", "y", "w", "h"])   
                        }
                        
                        return rval;
                    }
                    
                    return batch;           
                },   
                
                /** Registers a global listener for a given channel */		                  
                registerCallback: function(name, suffix) {
                    var ourname = "_" + name + "Listener";
                    
                    // Register the wrapper
                    this.variables.engine.registerListener(name, ourname);
                    callbacks.register(ourname, suffix);
                },
             
                      
                /** Starts a new batch call */
                startBatch: function(){
                    this.variables.batch = this.batch()
                },
                
                
                /** Ends the current batch call and executes it */
                endBatch: function(){
                    var batch = this.variables.batch,
                        engine = this.variables.engine
                    
                    if (!batch) return;
                    
                    // Execute each call
                    batch.generateBatchCalls().forEach(function(c){
                        engine.batch(c);
                    })

                    this.variables.batch = null;
                },
                
                
                handler: {
                    /** Generic callback function */
                    generic: function(listener, pnames) {
                        try {
                            var param = {};
                            
                            // Setup arguments
                            for(var i = 0; i<pnames.length; i++) {
                                param[pnames[i]] = arguments[2+i]
                            }
                            
                            // Call all listener
                            connector.variables.listeners.process(listener, function(f){
                                f(param);
                            });
                        } 
                        catch (e) {
                            alert(listener + " processing failure : " + e);
                        }	
                    },
                    
                    /** Called when reduced gaze events arrive */
                    onRawReducedGaze : function(x, y){
                        connector.connection.handler.generic("reducedApplicationGaze", ["x", "y"], parseInt(x), parseInt(y))
                    },              
                    
                    /** Called when fixation events arrive */
                    onRawFixation: function(_x, _y){
                        var x = parseInt(_x), 
                            y = parseInt(_y),
                            s = "fixationStart"
                        
                        if (x < 0 || y < 0) {
                            s = "fixationEnd"
                        }
                        
                        connector.connection.handler.generic("fixation", ["x", "y", "type"], x, y, s)
                    },
                    
                    /** Called when head movements arrives */
                    onRawHead: function(_date, _x, _y, _z){
                        connector.connection.handler.generic("headPosition", ["x", "y", "z"], parseFloat(_x), parseFloat(_y), parseFloat(_z))
                    },
                    
                    /** Called when a weak saccade occures */
                    onRawWeakSaccade: function(_angle, _distance){
                        connector.connection.handler.generic("weakSaccade", ["angle", "distance"], parseFloat(_angle), parseFloat(_distance))
                    },
                    
                    /** Called when the plugin is ready */
                    onRawPerusal: function(_speed, _x, _y, _w, _h, text){
                        connector.connection.handler.generic("perusal", ["speed", "x", "y", "w", "h", "text"], parseFloat(_speed), parseInt(_x), parseInt(_y), parseInt(_w), parseInt(_h), text)
                    },		
                },
                
                /** Called when the plugin is ready */
                onStatus: function(status){
                    
                    // Get a number variables
                    var self = text20.connector.connection,
                        engine = self.variables.engine,
                        registry = text20.connector.extensions.registry
                    
                    try {
                        if (status == "INITIALIZED") {
                            
                            self.updateaLoadingStatus("Lifesign received. Performing initialization.");
                            self.variables.initialized = true;
                            
                            // Check if the engine is there (this should never happen)
                            if(engine == null) {
                                alert("A strange error happened. Engine appears to be null, even though we got an initialization message!")
                                return;                 
                            }
            
                            self.updateaLoadingStatus("Loading extensions. Wish me luck.");
                            
                            // Check if we can query extensions (what is causing these problems?!!?)
                            if(engine.getExtensions == null) {
                                alert("getExtensions() not found. All extensions are disabled! Please report this bug!");
                            } else {
                                // Hook up all extensions 
                                var allExtensions = engine.getExtensions();
                                for(var i = 0; i<allExtensions.size(); i++) {
                                    var ext = allExtensions.get(i);
                                    
                                    // LiveConnect bullshit once again. On some platforms automatic wrapping of returend
                                    // objects doesn't work anymore. 
                                    if(ext && ext.toString) {
                                        ext = ext.toString();
                                    }
                                    
                                    registry.register(ext);
                                }                   
                            }

                            
                            self.updateaLoadingStatus("Loading calibration.");

                            
                            var browserID = text20.browser.id(),
                                dx = engine.getPreference("connector:calibration:" + browserID + ":offset:x", "UNDEFINED"),
                                dy = engine.getPreference("connector:calibration:" + browserID + ":offset:y", "UNDEFINED")
            
            
                            // If something has been set, use the override offset
                            if(dx != "UNDEFINED" && dy != "UNDEFINED") {
                                var newoffset = [0, 0];
                                newoffset[0] = parseInt(dx);
                                newoffset[1] = parseInt(dy);
                                
                                // Only use the new offset if there was no previous override set 
                                text20.browser.calibratedOffset = newoffset;								
                            }               
                                            
                            
                            self.updateaLoadingStatus("Extensions added. Registering special callbacks.");
            
                                            
                            // Register some of the global callback listeners at the engine ... 
                            // TODO: Only register the high volume listeners if there is a need
                            // self.registerCallback("reducedApplicationGaze", self.handler.onRawReducedGaze);
                            // self.registerCallback("headPosition", self.handler.onRawHead);
                            self.registerCallback("fixation", self.handler.onRawFixation);
                            self.registerCallback("perusal", self.handler.onRawPerusal);
                            self.registerCallback("weakSaccade", self.handler.onRawWeakSaccade);
                            
                            self.updateaLoadingStatus("Callbacks registered. Transfering control to client. Cu ;-)");
                                            
                            // Disable background
                            if(self.variables.loadIndicator) {
                                $("#atloadingindicatorbackground").hide()
                                $("#atloadingindicator").hide()                                         
                            }
                            
                            
                            // Setup Emotion Engine :-)
                            if(connector.config.enableBrainTracker) {
                            	var r = connector.extensions.brainTrackerInitEvaluation();
                            	alert(r)
                            }
							
							
							// Register mouse clicked listener
                            window.document.onclick = function(e) {
                                connector.extensions.mouseClicked(0, e.button)
                            }
                                                                        
                            // Process all initialized listener
                            connector.variables.listeners.process("INITIALIZED", function(f){
                                f();
                            });                                         
                        }
                    } 
                    catch (e) {
                        alert("statusListener() failure : " + e);
                    }
                },
                
                
                /** Called when a special callback is triggered by an extension */				
                onSpecialCallback: function(){
                    try {
                        var name = arguments[0],
                            rest = [];
                        
                        for (var i = 1; i < arguments.length; i++) {
                            rest.push(arguments[i])
                        }
                        
                        connector.variables.listeners.process("specialCallback", function(f){
                            f(name, rest)
                        });
                    } 
                    catch (e) {
                        alert("specialCallbackListener() failure : " + e);
                    }
                },


                /** Updates the loading status screen with the given message */                
                updateaLoadingStatus: function(status) {
                    if (this.variables.loadIndicator) $("#text20loadingindicator").html(status)
                },
                
                
                /** Connect the applet to the plugin, i.e., activate gaze! */
                connect: function(){
                    // In case we should render a load indicator    
                    if(this.variables.loadIndicator) {            
                        // Update the load indicator variable in case it is set.
                        loadIndicator = this.variables.loadIndicator;
                
                        // Append a background shade as well as a info box
                        $("body").append("<div id='text20indicatorbackground' style='z-index:100000; position:absolute; top:0px; left: 0px; width:100%; height:2000px; background:black; opacity:0.6'>&nbsp;</div>")         
                        $("body").append("<div " + 
                             "style='width:300px; height:120px; margin-left:-100px; position:absolute; background:orange; top:300px; opacity:0.97; left:50%; -webkit-border-radius:10px; z-index:100001' " +  
                             "id='text20loadingindicator'> " +
                                 "<br/>" +              
                                 "<center><span style='font-weight:bold; font-size:120%; text-decoration:underline; margin-left:20px; margin-top:40px;'>Connector Status</span></center>" +             
                                 "<br/>" +      
                                 "<center><span id='atloadingstatus'>Please wait ... initializing plugin.</span></center>" +
                         "</div>");             
                    }
                    
                    // Assemble extension string
                    var allExtensions = ""
                    connector.config.extensions.forEach(function f(i) {
                        allExtensions += file.absolutePath(i) + ";"
                    })
                    
                    // Append <applet> tag
                    $("body").append("<applet " +
                        "id='" + this.variables.appletID + "'" +
                        "name='Text20Engine'" +
                        "archive='" + connector.config.archive + "'" +
                        "code='de.dfki.km.text20.browserplugin.browser.browserplugin.impl.BrowserPluginImpl'" +
                        "width='1' height='1'" + "mayscript='true'" + ">" +
                            "<param name='trackingdevice' value='" + connector.config.trackingDevice + "'/>" +
                            "<param name='trackingconnection' value='" + connector.config.trackingURL + "'/>" +
                            "<param name='enablebraintracker' value='" + connector.config.enableBrainTracker + "'/>" +
                            "<param name='braintrackingconnection' value='" + connector.config.brainTrackingURL + "'/>" +
                            "<param name='callbackprefix' value='" + callbacks.prefix() + "'/>" +
                            "<param name='transmitmode' value='" + connector.config.transmitMode + "'/>" +
                            "<param name='sessionpath' value='" + connector.config.sessionPath + "'/>" +
                            "<param name='recordingenabled' value='" + connector.config.recordingEnabled + "'/>" +
                            "<param name='extensions' value='" + allExtensions + "'/>" +    
                            "<param name='logging' value='" + connector.config.logging + "'/>" +                
                    "</applet>");
                    
                    this.updateaLoadingStatus("Plugin added. Waiting for a lifesign. This usually takes 5-10 seconds.");
                    
                    // Set applet engine
                    this.variables.engine = $("#" + this.variables.appletID).get(0);
                    
                                           
                    // Prepare special callbacks
                    callbacks.register("_augmentedTextStatusFunction", this.onStatus)
                    callbacks.register("specialCallback", this.onSpecialCallback)
                    
                    // Initialize cache
                    this.variables.transmitCache.create("windowPosition")
                    this.variables.transmitCache.create("documentViewport")
                    this.variables.transmitCache.create("elementCache").cache = cache.cache()   
                },
                
                
                /** Check if the engine is running and up */
                enginecheck: function(fnc) {
                    if (!this.variables.initialized) {
                        alert("Enginecheck: Plugin not initialized yet");
                        return null;
                    }
                    
                    if (!this.variables.engine) {
                        alert("Enginecheck: There is no engine!");
                        return null;
                    }
                    
                    // If we got a function, call it
                    if(fnc) {
                        return fnc(this.variables.engine, this.variables, this)
                    } else return this.variables.engine;                    
                },
                
                
    
              
                /** Calls a generic command inside the engine */
                callGeneric: function(cmd){
                    return this.enginecheck(function(e, v, s) {
                        return e.callFunction(cmd)
                    })
                },
                
                
                /** Transmit if this window can be seen or not */
                transmitWindowVisibility: function(isVisible) {
                    this.enginecheck(function(e, v, s) {
                        e.updateElementFlag("#window", "focussed", isVisible)	
                    })
                },
                
               
                /** Transmits if the given element has been removed in the meantime */
                transmitElementRemoved: function(id) {
                    this.enginecheck(function(e, v, s) {
                        e.updateElementFlag(id, "REMOVED", true);
                    })
                },
                 
                
                /** Transmits where the element is anchored */
                transmitElementPositionAnchor: function(id, anchor) {
                    this.enginecheck(function(e, v, s) {
                        if (anchor == "window") 
                            e.updateElementFlag(id, "FIXED_ON_WINDOW", true);
                    })
                },

                
               /** Transmits the browser's outline */
               transmitBrowserGeometry: function(x, y, w, h) {
                    this.enginecheck(function(e, v, s) {
                        var p = v.transmitCache.get("windowPosition");
                        var offset = browser.documentOffset();
                        
                        // Correct offset before transmission
                        x += offset[0]
                        y += offset[1]
                        
                        // In case values have been changed
                        if (x != p.x || y != p.y || w != p.w || h != p.h) {
                            p.x = x;
                            p.y = y;
                            p.w = w;
                            p.h = h;
                            e.updateBrowserGeometry(x, y, w, h);
                        }
                    })
                },
                
                /** Sets or gets preferences */
                preference: function(key, value) {
                    return this.enginecheck(function(e, v, s) {
                        if(value) {
                            e.setPreference(key, value);
                            return                      
                        } else {
                           return e.getPreference(key, "UNDEFINED");
                        }
                    })
                },
                
                /** Transmits an element's outline */
                transmitElement: function(id, type, content, x, y, w, h) {
                    if(!this.enginecheck()) return
                    
                    var updateElementGeometry = this.variables.engine.updateElementGeometry

                    // If we have a batch, use that one					
                    if (this.variables.batch) {
                        updateElementGeometry = this.variables.batch.updateElementGeometry
                    }
                    
                    
                    var elementCache = this.variables.transmitCache.get("elementCache");
                    var cache = elementCache.cache.get(id)
                    
                    if (cache == null) {
                        cache = elementCache.cache.create(id);
                        cache.id = id;
                        cache.type = type;
                        cache.content = content;
                        cache.x = x;
                        cache.y = y;
                        cache.w = w;
                        cache.h = h;
                        
                        updateElementGeometry(id, type, content, x, y, w, h);
                        return;
                    }
                    
                    if (cache.x != x || cache.y != y || cache.w != w || cache.h != h) {
                        cache.x = x;
                        cache.y = y
                        cache.w = w;
                        cache.h = h;
                        updateElementGeometry(id, type, content, x, y, w, h);
                    }
                },
                
                /** Transmits the current viewport */
                transmitViewport: function(x, y){
                    if(!this.enginecheck()) return
                    
                    var p = this.variables.transmitCache.get("documentViewport");
                    
                    // In case values have been changed
                    if (x != p.x || y != p.y) {
                        p.x = x;
                        p.y = y;
                        this.variables.engine.updateDocumentViewport(x, y);
                    }
                },
                
                /** Transmit meta info */
                transmitElementMetaInformation: function(id, key, value) {
                    if(!this.enginecheck()) return
                        
                    if (this.variables.batch) {
                        this.variables.batch.updateElementMeta(id, key, value);
                        return;
                    }
                            
                    this.variables.engine.updateElementMetaInformation(id, key, value);
                },
               
                /** Drop calbration */
                dropBrowserCalibration: function() {
                     browser.overrideOffset = [0, 0];   
                },
               
                /** Sets a session parameter */
                setSessionParameter: function(key, value){
                    if(!this.enginecheck()) return
                    this.variables.engine.setSessionParameter(key, value);
                },
            }       
        },
    },
    
    core = {
        /** Configure core settings in here ... */
        config: {
            clusters: 1,                 // How many clusters we have for transmission

            // If set to true, the core will try to find all elements that have onGaze/onFixation/...
            // attributes. This works quite well, but takes some time every new fixation and might 
            // add significant overhead for larger pages. If set to false you have to call 
            // core.attributed.update('onFixation') -- (change onFixation with what you need) -- every
            // time you add or change the DOM tree and add or remove elements containing such attributes 
            autoupdateAttributed: true,
        },
        
                     
        /**  Our cache and handlers for attributed (onFixation, onGazeOver, ...) elements */ 
        attributed: {
            /** All attributes we should update */
            attributes: ["onGazeOver", "onGazeOut", "onFixation", "onPerusal"],
            
            /** Cache containing selectors for the given attributes */
            cache: {},
            
            /** Returns a jQuery object for the given selector */
            get: function(selector) {
                if(core.config.autoupdateAttributed) 
                    this.update(selector)					
                
                return this.cache[selector]
            },
            
            /** Updates the jquery object for the given selector */
            update: function(selector) {
                var cache = this.cache
                
                if(selector)
                    cache[selector] = $("*[" + selector + "]")
                else {
                    // Update values for each attribute
                    this.attributes.forEach(function f(x){
                        cache[x] = $("*[" + x + "]")    
                    })  
                }    
            }
        },

        
        /** Should not be access from the outside */
        internal: {
            clustering: {
                // Number of clusters to update 
                clusters: 1,
                
                // If this is set to true, elements will only be transmitted once. Useful if you know the page never changes.
                disableSubsequentUpdates: false,
                
                // Internal variables 
                clusterPTRCreation: 0,
                clusterPTRTransmission: 0,
                numRegistered: 0,
            },
            
            body: $("body").get(0),
            
            /** Update all previously registed elements */
            updateRegistered: function(){
                connector.connection.startBatch();
                
                var basics = this,
                    connection = connector.connection,
                    clustering = core.internal.clustering,
                    dontUpdateAfterRegister = true,
                    registered = false;
                
                // Process all elements which are currently unprocessed
                $(".untransmitted").each(function(){
                    var self = $(this)
                    var id = self.attr("id")
                    
                    var pos = dom.documentPosition(this),
                        w = parseInt(this.offsetWidth),
                        h = parseInt(this.offsetHeight),
                        type = "unknown",
                        content = null;
                    
                    var text = null,
                        word = null;
                        
                        
                    // Sanity check                        
                    if(pos == null) return;    
                    
                    
                    // Check type and content (SPAN-test)
                    if (this.firstChild && this.firstChild.nodeValue) {
                        type = "text"
                        content = this.firstChild.nodeValue;
                        
                        // Try to get optional values
                        text = self.attr("_textID")
                        word = self.attr("_wordID")
                    }
                    
                    // Transmit basic data first time
                    connection.transmitElement(id, type, content, pos[0], pos[1], w, h);
                    
                    // Transmit flags first time
                    if (pos[2] == "window") {
                        connection.transmitElementPositionAnchor(id, "window");
                    }
                    
                    // Transmit sequential text information
                    if (text != null) {
                        connection.transmitElementMetaInformation(id, "textID", text);
                        connection.transmitElementMetaInformation(id, "wordID", word);
                    }
                    
                    // Remove flag 
                    self.removeClass("untransmitted")
                    
                    // Rember that we registered an element
                    registered = true
                });
                
                // In case subsequent updates are disabled we are finished. 
                if (clustering.disableSubsequentUpdates || (dontUpdateAfterRegister && registered)) {
                    connector.connection.endBatch();
                    return;
                } 
                
                // TODO: Shouldn't we stop here when we added new elements, so that they don't get transmitted
                // twice? On the other hand, how would we know which these are?
                
                // Select what we want to transmit this round
                var cls = ".clusterID" + clustering.clusterPTRTransmission++;
                
                // Cycle clusterPTRTransmission
                if (clustering.clusterPTRTransmission == core.config.clusters) {
                    clustering.clusterPTRTransmission = 0;
                }
                
                // Neat. We just select all elements by their class and send them
                $(cls).each(function(){
                    var id = $(this).attr("id")
                    
                    var pos = dom.documentPosition(this),
                        w = parseInt(this.offsetWidth),
                        h = parseInt(this.offsetHeight);
                    
                    connection.transmitElement(id, null, null, pos[0], pos[1], w, h);
                });
                
                connector.connection.endBatch();
            }
        },
        
        handler: {
            /**
             * Handles onGaze handler given a gazed screen position.
             *
             * @param {Object} x position in document coordinates
             * @param {Object} y position in document coordinates
             */
            onGazeHandler: function(x, y){
                // After this we know all elements which are under gaze.
                var gazedElement = document.elementFromPoint(x - window.pageXOffset, y - window.pageYOffset),
                    allUnderCurrentGaze = dom.parents(gazedElement);


                // Or ther onGazeOut handler
                core.attributed.get("onGazeOut").each(function(i){
                    // If an element with onGazeOut is under gaze, ignore it. 
                    if (allUnderCurrentGaze.indexOf(this) >= 0) 
                        return;
                    
                    var elem = $(this)
                    
                    // If the was not under gaze last round, ignore it           
                    if (!elem.hasClass("underGazeLastRound")) 
                        return;
                    
                    // Okay, in this case we call the handler and set underGazeLastRound ...
                    eval(elem.attr("onGazeOut"))
                    elem.removeClass("underGazeLastRound")
                })
                
                
                // First we check which elements might need to have called their onGazeOver handler
                core.attributed.get("onGazeOver").each(function(i){
                    var elem = $(this)
                    
                    // If an element with onGazeOver isn't under gaze, ignore it. 
                    if (allUnderCurrentGaze.indexOf(this) < 0) {
                        elem.removeClass("underGazeLastRound")
                        return;
                    }
                    
                    // If it is, we have to check if it already was under gaze last time.           
                    if (elem.hasClass("underGazeLastRound")) 
                        return;
                        
                    // Okay, in this case we call the handler and set underGazeLastRound ...
                    eval(elem.attr("onGazeOver"))
                })

                // Mark all elements under gaze this round
                allUnderCurrentGaze.forEach(function f(x){
                    $(x).addClass("underGazeLastRound")
                })
            },
            
            
            /**
             * Handles onFixation handler.
             *
             * @param {Object} x
             * @param {Object} y
             */
            onFixationHandler: function(x, y){
                // After this we know all elements which are under gaze.                
                var gazedElement = document.elementFromPoint(x - window.pageXOffset, y - window.pageYOffset),
                    allUnderCurrentGaze = dom.parents(gazedElement);
                
                core.attributed.get("onFixation").each(function(i){
                    // If an element with onGazeOut is under gaze, ignore it. 
                    if (allUnderCurrentGaze.indexOf(this) < 0) 
                        return;
                    
                    var elem = $(this)
                    
                    // Okay, in this case we call the handler and set underGazeLastRound ...
                    eval(elem.attr("onFixation"))
                })
            },
            
            /**
             * Handles onPerusal events.
             *
             * @param {Object} x
             * @param {Object} y
             */
            onPerusalHandler: function(e){
            
                core.attributed.get("onPerusal").each(function(i){
                
                    var pos = dom.documentPosition(this);
                    var x = pos[0]
                    var y = pos[1]
                    var w = parseInt(this.offsetWidth);
                    var h = parseInt(this.offsetHeight);
                    
                    var match = false
                    
                    
                    //
                    // 1. See if the element intersects the perused rectangle  
                    // 
                    if (e.x <= x && x <= e.x + e.w &&
                    e.y <= y &&
                    y <= e.y + e.h) {
                        match = true;
                    }
                    if (e.x <= x + w && x + w <= e.x + e.w &&
                    e.y <= y &&
                    y <= e.y + e.h) {
                        match = true;
                    }
                    if (e.x <= x && x <= e.x + e.w &&
                    e.y <= y + h &&
                    y + h <= e.y + e.h) {
                        match = true;
                    }
                    if (e.x <= x + w && x + w <= e.x + e.w &&
                    e.y <= y + h &&
                    y + h <= e.y + e.h) {
                        match = true;
                    }
                    
                    if (x <= e.x && e.x <= x + w &&
                    y <= e.y &&
                    e.y <= y + h) {
                        match = true;
                    }
                    
                    if (match) {
                    
                        //
                        // 2. If it does, additionally check if the elment was visible 
                        //
                        // FIXME: Does not work properly. We have to hit the element, then get all of its parents
                        // and again, check if they might be equal.
                        //var hitTestResult = document.elementFromPoint(x + w / 2, y + h / 2);
                        //if(hitTestResult != this) return;
                        
                        var elem = $(this)
                        eval(elem.attr("onPerusal"))
                    }
                })
             },
             
             /**
              * Handles onEmotion handler.
              *
              * @param {Object} x
              * @param {Object} y
              */
             onEmotionHandler: function(x, y){
                 // After this we know all elements which are under gaze.                
                 var gazedElement = document.elementFromPoint(x - window.pageXOffset, y - window.pageYOffset),
                     allUnderCurrentGaze = dom.parents(gazedElement),
                     emotion = connector.extensions.getTrainedPeakEmotion();	
                 
                 if (!emotion) return; 
                 
                 var map = { 
                	 "happy" : "onSmile", 
                	 "interested" : "onInterest", 
                	 "doubt" : "onFurrow", 
                	 "bored" : "onBoredom", 
                 }
                 
                 var handler = map[emotion.split(" ")[0]]; 
                 if (!handler) return;

              	 core.attributed.get(handler).each(function(i){
                     if (allUnderCurrentGaze.indexOf(this) < 0) 
                         return;
                     
                     eval($(this).attr(handler))
                 })                	 
             }
        },
        
        /** Handles connector callbacks */
        listener: {
            
            /** Called upon init of the connector. */
            initListener: function(){
                var connection = connector.connection
                
                // Register handler after a short timeout, otherwise we might get spurious blur events ...       
                $(window).oneTime(250, function(){
                    $(this).blur(callbacks.func("blurListener"));
                    $(this).focus(callbacks.func("focusListener"));
                });
                
                // Update the browser position regularly.
                $(window).everyTime(250, "", function() {
                
                    // TODO: Get the values, otherwise gaze posistion will be wrong ...         
                    var offsetX = 0;
                    var offsetY = 0;
                    
                    var trueX = window.screenX + offsetX;
                    var trueY = window.screenY + offsetY;
                    
                    connection.transmitBrowserGeometry(trueX, trueY, window.innerWidth, window.innerHeight);
                    connection.transmitViewport(window.pageXOffset, window.pageYOffset);
                });
            },
        
            /** Called when new fixations come in */
            fixationListener: function(param){
                if (param.type != "fixationStart") 
                    return;
                
                var x = param.x;
                var y = param.y;
                
                // call onGaze* and onFixation handler
                core.handler.onGazeHandler(x, y)
                core.handler.onFixationHandler(x, y)

                core.handler.onEmotionHandler(x, y)
            },
            
            /** Called when new reduced gaze events come in */			
            reducedGazeListener: function(param){
                var x = param.x;
                var y = param.y;
            },
            
            /** Called when new perusal events come in */			
            perusalListener: function(param){
                core.handler.onPerusalHandler(param);
            },
        },
       
        /**
         * Call this to register an element for advanced gaze tracking (i.e.,
         * transmition to the atplugin). onGaze handler will work without, but, for example,
         * the reading detection requires words.
         *
         * Elements not registered are invisible to the engine.
         *
         * @param {Object} element
         */
        register: function(element){
            if(!element) return;
            
            if(!element.forEach) {
                element = [element]
            }
            
            var c = this.internal.clustering
            
            element.forEach(function(_e) {
                var e = $(_e)
                
                // Tag and ensure it has an ID
                e.addClass("registeredGazeElement")
                e.addClass("untransmitted")
                e.addClass("clusterID" + c.clusterPTRCreation++)
                
                // Cycle clusterPTR creation
                if (c.clusterPTRCreation == core.config.clusters) {
                    c.clusterPTRCreation = 0;
                }
                
                dom.ensureID(_e);
            })
        },
        
        /**
         * Unregisters an element and makes it invisible to the engine again.
         *
         * As a rule of thum, user-controlled sites should register element as soon as they were
         * created and deregister them only shortly before removal.
         *
         * @param {Object} element
         */
        deregister: function(element){
            if(!element) return;
            
            if(!element.forEach) {
                element = [element]
            }
                
            element.forEach(function(e) {
                var el = $(e)
                el.removeClass("registeredGazeElement")
                connector.connection.transmitElementRemoved(el.attr("id"));
            })				            
        },

        
        /** Call when everything is set up and ready to go */
        init: function() {
            
            var self = this
            
            // Execute this only when the document is ready ...
            $(document).ready(function() {
                var connection = connector.connection 
                
                // Bring up the currently used connection
                connection.connect()

                // Update element positions regularly        
                $(window).everyTime(1000, "", self.internal.updateRegistered);
                
                // Add initialization listener to the connector
                connector.listener("INITIALIZED", self.listener.initListener);
                connector.listener("fixation", self.listener.fixationListener);
                connector.listener("perusal", self.listener.perusalListener);
                
                // Register some global callbacks regarding the window visibility 
                callbacks.register("blurListener", function(){
                    connection.transmitWindowVisibility(false);
                });
                callbacks.register("focusListener", function(){
                    connection.transmitWindowVisibility(true);
                });	
                
                // Update all attributed elements
                core.attributed.update()
            })
        }
    }
    
    
    // Add namespaces
    text20.math = math
    text20.listener = listener
    text20.system = system
    text20.browser = browser   
    text20.dom = dom
    text20.file = file
    text20.connector = connector
    text20.core = core
    
    // Set the default connection
    connector.connection = connector.methods.applet

    // Register text20 globally
    window.text20 = text20
})(window);
