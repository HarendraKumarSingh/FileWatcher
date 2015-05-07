# FileWatcher
Using Java 7 API here created a file system functionalities to watch the file system for changes. Now, we can watch for events like creation, deletion, modification, and get involved with our own actions.

The java.nio.file package provides a file change notification API, called the Watch Service API. This API enables you to register a directory (or directories) with the watch service. When registering, you tell the service which types of events you are interested in: file creation, file deletion, or file modification. When the service detects an event of interest, it is forwarded to the registered process. The registered process has a thread (or a pool of threads) dedicated to watching for any events it has registered for. When an event comes in, it is handled as needed.

#Requirements for implementation 
<ul>
<li>An object implementing the Watchable interface - the Path class is perfect for this job.</li>
<li>A set of events that we are interested in(file creation, file deletion, or file modification) - we will use StandardWatchEventKind which implements the WatchEvent.</li>
<li>An event modifier that qualifies how a Watchable is registered with a WatchService</li>
<li>Finally a Watcher who watches the file syatem for any changes</li>
</ul>

#FileWatcher implementation
<ul>
<li><p>Create a WatchService "watcher" for the file system.</p></li>
<li><p>For each directory that you want monitored, register it with the watcher. When registering a directory, you specify the type of events for which you want notification.</p></li>
<li><p>When registering an object with the watch service, you specify the types of events that you want to monitor. The supported StandardWatchEventKinds event types : 
<ul>
<li><code>ENTRY_CREATE</code> – A directory entry is created.</li>
<li><code>ENTRY_DELETE</code> – A directory entry is deleted.</li>
<li><code>ENTRY_MODIFY</code> – A directory entry is modified.</li>
<li><code>OVERFLOW</code> – Indicates that events might have been lost or discarded. You do not have to register for the <code>OVERFLOW</code> event to receive it.</li>
</ul></p></li>
<li><p>You receive a WatchKey instance for each directory that you register.</p>
<p>
Implement an infinite loop to wait for incoming events. When an event occurs, the key is signaled and placed into the watcher's queue.</p></li>
<li><p>Retrieve the key from the watcher's queue. You can obtain the file name from the key.</p></li>
<li><p>Retrieve each pending event for the key (there might be multiple events) and process as needed.Reset the key, and resume waiting for events.</p></li>
<li><p>Close the service</p></li>
</ul>
</br>

<p>The FileSystem object and the WatchService can also be created like this:</p>

<div class="highlight highlight-bash"><pre>FileSystem fileSystem = FileSystems.getDefault();
WatchService watcher = fileSystem.newWatchService();</pre>
<p>OR both in one line</p>
<pre>watchService = FileSystems.getDefault().newWatchService();</pre></div>

#When to Use This API
<p>The Watch Service API is designed for applications that need to be notified about file change events. It is well suited for any application, like an editor or IDE, that potentially has many open files and needs to ensure that the files are synchronized with the file system. It can also be used to create cutom File System river which watches on a file system directory for any change and on change it re-indices the change into Elasticsearch.</p>
