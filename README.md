# `🔍` MobLister

A simple Fabric 1.20.1 mod to list all registered mob types and possible duplicates.

While making a modpack, you might want to know what mobs you ended up with, and if there might be any duplicates.
This mod was specifically made for that.

I hacked this together as my first Fabric mod, so feel free to open issues if you find any bugs.

## Usage

1. Add the mod (doesn't have any dependency, including Fabric API)
2. Use the `/moblister list` command to list all registered mobs. The list may be big depending on the modpack, so you
   might need to check your logs if it's too long to fit in chat.
3. Use the `/moblister duplicates` command to list all duplicates. Like above, might need to check your logs depending
   on the size.