# MidiMusicNetwork
An interactive network of nodes playing music.

# Motivation
After watching Mark Browns' video on game controllers: https://www.youtube.com/watch?v=VJGKDyrR8qc, I realized something. If you don't have time to watch the video, here is a quick explanation: When developing games, the controller that is used to play game sets a lot of limitations on what is possible. The XBox controller is really good at moving a character around in 2D and 3D space, but it isn't suited for a lot of other genres like real time strategy games. This is also true when making music. Trying to create a melody on a trombone is a lot different from creating one while singing or playing the piano. I thought I wanted to explore this by creating a software instrument that has different limitations than other instruments. The instrument makes some things easier to do, like playing chords and melodies in a repeating pattern, but other things are not that easy to do. Some things are even impossible to do. In the future I want to create music with this instrument and upload it here.

# How it works
Nodes can be placed on a grid and be connected to one another. When a node is pressed, it sends a package to all nodes connected to this node. Whenever a node receives a package it sends a midi-signal based on some data on the node object. If you have another program open that can listen for midi-signals, that program can be used with this one. I'm using it with Logic X to create arppeggios and small repeating melodies.

# How to use


# Showcase


