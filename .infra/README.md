### Env variables substitution 
Configuration files don't natively supports envars. 
The solution is to write the configuration file as if it can resolve the envars and then, before using
it resolve the envars using `envsubst` ( check the better solution at the end)

#### How to install 
With homebrew use
```shell
brew install gettext
brew link --force gettext 
```

#### How to use
Just invoke 'envsubst' passing the source and the destination.
```shell
envsubst < "source.txt" > "destination.txt"
```

Resolved file should not be commited to git so ignore it.
In the `.gitignore` file there is the following rule to prevent the commit of "resolved" files
So please name resolved file following this rule. 

```gitignore
*.resolved.*
```

To fast up this process running the `vars.sh` script you can use the command `envresolve`
just passing the source file path. The output will be in the same location, with the same name but with
the `resolved` postfix 

```shell
envresolve "/myfolder/source.txt"
## output file: /myfolder/source.resolved.txt

## -- OR --
SOURCE_FILE=$(envresolve "source.yml")
```

