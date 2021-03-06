# MidiMusicNetwork
An interactive network of nodes playing music.

## Motivation
After watching Mark Browns' video on game controllers: https://www.youtube.com/watch?v=VJGKDyrR8qc, I realized something. If you don't have time to watch the video, here is a quick explanation: When developing games, the controller that is used to play game sets a lot of limitations on what is possible. The XBox controller is really good at moving a character around in 2D and 3D space, but it isn't suited for a lot of other genres like real time strategy games. This is also true when making music. Trying to create a melody on a trombone is a lot different from creating one while singing or playing the piano. I thought I wanted to explore this by creating a software instrument that has different limitations than other instruments. The instrument makes some things easier to do, like playing chords and melodies in a repeating pattern, but other things are not that easy to do. Some things are even impossible to do.

## How it works
Nodes can be placed on a grid and be connected to one another. When a node is pressed, it sends a package to all nodes connected to this node. Whenever a node receives a package it sends a midi-signal based on some data on the node object. If you have another program open that can listen for midi-signals, that program can be used with this one. I'm using it with Logic X to create arppeggios and small repeating melodies.

## How to use
### Mouse actions
Left-click on an empty region to create a node. Left click on an exsisting node to play that node. Middle-click to delete the node. Scroll on the node to change its' pitch (You can also use the arrow-keys for this). Drag with right-click to establish a connection between this node and another one.

### Keyboard
While hovering over a node, press 'm' to mute it and 'i' to make it receive packages from other nodes instantly. 
You can also bind keys 'a', 's', 'd', 'f', 'g' to play certain nodes. Hover the mouse over a node, then hold shift and press the key you want to bind.
Hold space to enable sustain (Works like the sustain pedal on a piano). 
Press c to clear all nodes.

## Showcase
For now I have created a little track that demonstrates what the program can do. This is what the program looked like when I created it:

![This what it looks like](/screenshot.png)

Here you can listen to it:
https://soundcloud.com/grunkn/midinetworkexperiment

As you can hear, the beat is not steady at all and the melody is not really that good either. I hope to improve on this in the future.
