package net.aufdemrand.denizen.requirements;

import java.rmi.activation.ActivationException;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;

import net.aufdemrand.denizen.Denizen;
import net.aufdemrand.denizen.requirements.core.FlaggedRequirement;
import net.aufdemrand.denizen.requirements.core.HeroesRequirement;
import net.aufdemrand.denizen.requirements.core.LiquidRequirement;
import net.aufdemrand.denizen.requirements.core.PoweredRequirement;

public class RequirementRegistry {

	private Map<String, AbstractRequirement> requirements = new HashMap<String, AbstractRequirement>();
	private Map<Class<? extends AbstractRequirement>, String> requirementsClass = new HashMap<Class<? extends AbstractRequirement>, String>();
	public Denizen plugin;


	public RequirementRegistry(Denizen denizen) {
		plugin = denizen;
	}

	public boolean registerRequirement(String requirementName, AbstractRequirement requirementClass) {
		this.requirements.put(requirementName.toUpperCase(), requirementClass);
		this.requirementsClass.put(requirementClass.getClass(), requirementName.toUpperCase());
		plugin.getLogger().log(Level.INFO, "Loaded " + requirementName + " Requirement successfully!");
		return true;
	}


	public Map<String, AbstractRequirement> listRequirements() {
		return requirements;
	}

	
	public <T extends AbstractRequirement> T getRequirement(Class<T> theClass) {
		if (requirementsClass.containsKey(theClass))
			return (T) theClass.cast(requirements.get(requirementsClass.get(theClass)));
		else
			return null;
	}
	
	public AbstractRequirement getRequirement(String requirementName) {
		if (requirements.containsKey(requirementName.toUpperCase()))
			return requirements.get(requirementName);
		else
			return null;
	}

	public void registerCoreRequirements() {

		FlaggedRequirement flaggedRequirement = new FlaggedRequirement();
		PoweredRequirement poweredRequirement = new PoweredRequirement();
		LiquidRequirement liquidRequirement = new LiquidRequirement();
		HeroesRequirement heroesRequirement = new HeroesRequirement();
		
		plugin.getLogger().info("Loading LEGACY requirements...DONE!");
		
		try {
			heroesRequirement.activateAs("HEROESCLASS");
			flaggedRequirement.activateAs("FLAGGED");
			poweredRequirement.activateAs("ISPOWERED");
			liquidRequirement.activateAs("ISLIQUID");
		} catch (ActivationException e) {
			plugin.getLogger().log(Level.SEVERE, "Oh no! Denizen has run into a problem registering the core requirements!");
			e.printStackTrace();
		}
		
		
	}


}