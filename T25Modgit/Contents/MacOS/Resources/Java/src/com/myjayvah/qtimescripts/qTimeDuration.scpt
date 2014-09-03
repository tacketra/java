tell application "QuickTime Player"
     #get properties
     #get properties of the front window
     #get MovieDuration of the front window
     #set dursecs to (duration of document 1) / 1000
     set dury to current time of document 1
     return dury
end tell