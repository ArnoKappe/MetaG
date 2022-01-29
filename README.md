# MetaG
False-positive results occur in digital metagenomic sequencing of bacteria. **MetaG** significantly reduces these and optionally outputs analysis charts as Scalable Vector Graphics.

## Get the app
Prebuilt archives with GUI (graphical user interface) are available:
### Windows
- With Java (recommended): [`MetaG-2022.1.0-win.zip`](https://github.com/dluxe0/MetaG/releases/download/2022.1.0/MetaG-2022.1.0-win.zip)
- Without Java: [`MetaG-2022.1.0-nojava-win.zip`](https://github.com/dluxe0/MetaG/releases/download/2022.1.0/MetaG-2022.1.0-nojava-win.zip)

### Linux
- With Java (recommended): [`MetaG-2022.1.0-linux.zip`](https://github.com/dluxe0/MetaG/releases/download/2022.1.0/MetaG-2022.1.0-linux.zip)
- Without Java: [`MetaG-2022.1.0-nojava-linux.zip`](https://github.com/dluxe0/MetaG/releases/download/2022.1.0/MetaG-2022.1.0-nojava-linux.zip)

### Alternative
Alternatively you could [build](BUILD.md) the app with the source code.
- Release: [`MetaG-2022.1.0.zip`](https://github.com/dluxe0/MetaG/archive/refs/tags/2022.1.0.zip)
- Current: [`MetaG-main.zip`](https://github.com/dluxe0/MetaG/archive/refs/heads/main.zip)

## Run the app
> For the version without java make sure your JAVA_HOME variable is set to jdk 17+

> For self-built version see [`BUILD.md`](BUILD.md)
### Windows
1. Unzip into a folder of your choice
2. Run `bin/MetaG.bat`

### Linux
1. Unzip into a folder of your choice
2. Run `bin/MetaG`

## Settings
In order to achieve the best possible results, you can make settings for how MetaG works. In the regular version with GUI, the layout elements can be used for this, alternatively and in the version without GUI, the `MetaG.properties` file is intended for this.
|    Setting                                  |   Specification                                          |   Value
| ------------------------------------------- |----------------------------------------------------------|---------------------------------------------
| DIRECTORY                                   | Path for databases                                       |use `\\` or `/` for folders
| BROWSE_SUBFOLDERS                           | Whether subfolders should be searched for files          |`true` or `false`
| BAR_COUNT                                   | The number of columns in the diagram                     |`10` - `50` others are experimental
| MAX_POSITIONS                               | The upper limit of reads to process                      |at least `1000`, recommended `10000`
| GRAPH_SCALE                                 | The scaling of the diagram                               |`1`,`2`,`5`,`10`,`20` others are experimental
| SVG_OUTPUT                                  | Whether to create SVGs                                   |`true` or `false`
> Note: `MetaG.properties` is created automatically on first run.

`MetaG.properties` could look like the following

    #MetaG.properties
    #Sat Jan 29 13:26:17 CET 2022
    BAR_COUNT=50
    DIRECTORY=D\:\\MetaG\\Data\\
    MAX_POSITIONS=10000
    BROWSE_SUBFOLDERS=true
    SVG_OUTPUT=false
    GRAPH_SCALE=1
