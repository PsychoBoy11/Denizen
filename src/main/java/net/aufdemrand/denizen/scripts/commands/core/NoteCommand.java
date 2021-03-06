package net.aufdemrand.denizen.scripts.commands.core;

import net.aufdemrand.denizen.exceptions.CommandExecutionException;
import net.aufdemrand.denizen.exceptions.InvalidArgumentsException;
import net.aufdemrand.denizen.objects.*;
import net.aufdemrand.denizen.objects.notable.Notable;
import net.aufdemrand.denizen.scripts.ScriptEntry;
import net.aufdemrand.denizen.scripts.commands.AbstractCommand;
import net.aufdemrand.denizen.tags.ObjectFetcher;
import net.aufdemrand.denizen.utilities.debugging.dB;


public class NoteCommand extends AbstractCommand {

    @Override
    public void parseArgs(ScriptEntry scriptEntry) throws InvalidArgumentsException {

        for (aH.Argument arg : aH.interpret(scriptEntry.getArguments())) {

            if (arg.matchesPrefix("as, i, id"))
                scriptEntry.addObject("id", arg.asElement());

            else if (ObjectFetcher.canFetch(arg.getValue().split("@")[0]))
                scriptEntry.addObject("object", arg.getValue());

        }

        if (!scriptEntry.hasObject("id") || !scriptEntry.hasObject("object"))
            throw new InvalidArgumentsException("Must specify an id and fetchable-object to note.");

    }

    @Override
    public void execute(ScriptEntry scriptEntry) throws CommandExecutionException {

        String object = (String) scriptEntry.getObject("object");
        Element id = scriptEntry.getElement("id");

        dB.report(getName(), aH.debugObj("object", object) + id.debug());

        String object_type = object.split("@")[0].toLowerCase();
        Class object_class = ObjectFetcher.getObjectClass(object_type);

        if (object_class == null) {
            dB.echoError("Invalid object type! Could not fetch '" + object_type + "'!");
            return;
        }

        dObject arg;
        try {

            if ((Boolean) object_class.getMethod("matches", String.class)
                    .invoke(null, object) == false) {
                dB.echoDebug("'" + object
                        + "' is an invalid " + object_class.getSimpleName() + ".");
                return;
            }

            arg = (dObject) object_class.getMethod("valueOf", String.class)
                    .invoke(null, object);

            if (arg instanceof Notable)
                ((Notable) arg).makeUnique(id.asString());

        } catch (Exception e) {
            dB.echoError("Uh oh! Report this to aufdemrand! Err: TagManagerObjectReflection");
            e.printStackTrace();
        }


    }



}