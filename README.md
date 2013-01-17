GISSystem
=========

An academic project. Given certain feature of a GIS record, retrieve all the records matching the given feature.

The execution of the program will be driven by a script file. Lines beginning with a semicolon character (';') are comments 
and should be ignored.  Each non-comment line of the command file will specify one of the commands described below.  

Each line consists of a sequence of tokens, which will be separated by single tab characters. A newline character will 
immediately follow the final token on each line. The command file is guaranteed to conform to this specification, so you do 
not need to worry about error-checking when reading it. The following commands must be supported: 

    world&lt;tab&gt;&lt;westLong&gt;&lt;tab&gt;&lt;eastLong&gt;&lt;tab&gt;&lt;southLat&gt;&lt;tab&gt;&lt;northLat&gt; 

This will be the first command in the file, and will occur once.  It specifies the boundaries of the coordinate space to be 
modeled.  The four parameters will be longitude and latitudes expressed in DMS format, representing the vertical and 
horizontal boundaries of the coordinate space. 

It is possible that the GIS record file will contain records for features that lie outside the specified coordinate space.  
Such records should be ignored; i.e., they will not be indexed. 

    import&lt;tab&gt;&lt;GIS record file&gt; 

Add all the GIS records in the specified file to the database file.  This means that the records will be appended to the 
existing database file, and that those records will be indexed in the manner described earlier.  When the import is 
completed, log the number of entries added to each index, and the longest probe sequence that was needed when 
inserting to the hash table. 

    what_is_at&lt;tab&gt;&lt;geographic coordinate&gt; 

For every GIS record in the database file that matches the given &lt;geographic coordinate&gt;, log the offset at 
which the record was found, and the feature name, county name, and state abbreviation.  Do not log any other data 
from the records. 

    what_is_at&lt;tab&gt;-l&lt;tab&gt;&lt;geographic coordinate&gt; 

For every GIS record in the database file that matches the given &lt;geographic coordinate&gt;, log every 
important non-empty field, nicely formatted and labeled.  See the posted log files for an example.  Do not log any 
empty fields. 

    what_is_at&lt;tab&gt;-c&lt;tab&gt;&lt;geographic coordinate&gt; 

Log the number of GIS records in the database file that match the given &lt;geographic coordinate&gt;.  Do not 
log any data from the records themselves. 

    what_is&lt;tab&gt;&lt;feature name&gt;&lt;tab&gt;&lt;state abbreviation&gt;

For every GIS record in the database file that matches the given &lt;feature name&gt; and &lt;state 
abbreviation&gt;, log the offset at which the record was found, and the county name, the primary latitude, and the 
primary longitude.  Do not log any other data from the records. 

    what_is&lt;tab&gt;-l&lt;feature name&gt;&lt;tab&gt;&lt;state abbreviation&gt; 

For every GIS record in the database file that matches the given &lt;feature name&gt; and &lt;state 
abbreviation&gt;, log every important non-empty field, nicely formatted and labeled.  See the posted log files for an 
example.  Do not log any empty fields. 

    what_is&lt;tab&gt;-c&lt;feature name&gt;&lt;tab&gt;&lt;state abbreviation&gt; 

Log the number of GIS record in the database file that match the given &lt;feature name&gt; and &lt;state 
abbreviation&gt;.  Do not log any data from the records themselves.

    what_is_in&lt;tab&gt;&lt;geographic coordinate&gt;&lt;tab&gt;&lt;half-height&gt;&lt;tab&gt;&lt;half-width&gt; 

For every GIS record in the database file whose coordinates fall within the closed rectangle with the specified height 
and width, centered at the &lt;geographic coordinate&gt;, log the offset at which the record was found, and the 
feature name, the state name, and the primary latitude and primary longitude. Do not log any other data from the 
records.  The half-height and half-width are specified as seconds. 

    what_is_in&lt;tab&gt;-l&lt;tab&gt;&lt;geographic coordinate&gt;&lt;tab&gt;&lt;half-height&gt;&lt;tab&gt;&lt;half-width&gt; 

For every GIS record in the database file whose coordinates fall within the closed rectangle with the specified height 
and width, centered at the &lt;geographic coordinate&gt;, log every important non-empty field, nicely formatted 
and labeled.  See the posted log files for an example.  Do not log any empty fields.  The half-height and half-width are 
specified as seconds. 

    what_is_in&lt;tab&gt;-c&lt;tab&gt;&lt;geographic coordinate&gt;&lt;tab&gt;&lt;half-height&gt;&lt;tab&gt;&lt;half-width&gt; 

Log the number of GIS records in the database file whose coordinates fall within the closed rectangle with the 
specified height and width, centered at the &lt;geographic coordinate&gt;. Do not log any data from the records 
themselves.  The half-height and half-width are specified as seconds. 

    debug&lt;tab&gt;[ quad | hash | pool ] 

Log the contents of the specified index structure in a fashion that makes the internal structure and contents of the index 
clear.  It is not necessary to be overly verbose here, but it would be useful to include information like key values and 
file offsets where appropriate. 

    quit&lt;tab&gt; 

Terminate program execution.  

If a &lt;geographic coordinate&gt; is specified for a command, it will be expressed as a pair of latitude/longitude values, 
expressed in the same DMS format that is used in the GIS record files. 
For all the commands, if a search results in displaying information about multiple records, you may display that data in any 
order that is natural to your design. 
