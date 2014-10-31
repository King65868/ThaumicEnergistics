package thaumicenergistics.fluids;

import java.util.LinkedHashMap;
import java.util.Map.Entry;
import net.minecraft.block.Block;
import net.minecraft.util.StatCollector;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;
import thaumcraft.api.aspects.Aspect;
import thaumicenergistics.ThaumicEnergistics;
import thaumicenergistics.api.TEApi;
import thaumicenergistics.api.interfaces.IEssentiaGas;
import cpw.mods.fml.common.FMLLog;

/**
 * A fluid which represents the gas form of an essentia.
 * 
 * @author Nividica
 * 
 */
public class GaseousEssentia
	extends Fluid
	implements IEssentiaGas
{
	/**
	 * List of all created gasses.
	 */
	public static final LinkedHashMap<Aspect, GaseousEssentia> gasList = new LinkedHashMap<Aspect, GaseousEssentia>();

	/**
	 * The aspect the gas is based off of.
	 */
	private Aspect associatedAspect;

	/**
	 * Creates the gas.
	 * 
	 * @param gasName
	 * Name of the gas displayed to the user
	 * @param aspect
	 * The aspect the gas is based off of.
	 */
	private GaseousEssentia( final String gasName, final Aspect aspect )
	{
		// Pass to super
		super( gasName );

		// Set the aspect
		this.associatedAspect = aspect;

		// Gas slightly glows
		this.setLuminosity( 7 );

		// Negative density, it floats away!
		this.setDensity( -4 );

		// Flow speed, 3x slower than water
		this.setViscosity( 3000 );

		// This is a gas, adjusts the render pass.
		this.setGaseous( true );

		// If the gas ever makes its way out of the AE system, disperse.
		this.setBlock( Block.getBlockFromName( "air" ) );

	}

	/**
	 * Creates a gas based on the specified aspect
	 * 
	 * @param aspect
	 */
	private static void create( final Aspect aspect )
	{
		// Ensure this has not already been register
		if( gasList.containsKey( aspect ) )
		{
			// Return the existing fluid
			return;
		}

		// Create the name
		String gasName = "gaseous" + aspect.getTag() + "essentia";

		// Create the fluid
		GaseousEssentia newGas = new GaseousEssentia( gasName, aspect );

		// Register the fluid
		if( FluidRegistry.registerFluid( newGas ) )
		{
			// Add to the list
			gasList.put( aspect, newGas );

			// Add to api
			TEApi.instance.essentiaGases.add( newGas );

			// Log info
			FMLLog.info( "%s: Created fluid for aspect %s.", ThaumicEnergistics.MOD_ID, aspect.getTag() );
		}
		else
		{
			// Log a warning
			FMLLog.warning( "%s: Unable to register '%s' as fluid.", ThaumicEnergistics.MOD_ID, aspect.getTag() );
		}

	}

	/**
	 * Gets the gas form of the specified aspect
	 * 
	 * @param aspect
	 * @return
	 */
	public static GaseousEssentia getGasFromAspect( final Aspect aspect )
	{
		return GaseousEssentia.gasList.get( aspect );
	}

	/**
	 * Called from load to register all gas types with the game.
	 */
	public static void registerGases()
	{
		// Create a gas for each essentia type
		for( Entry<String, Aspect> aspectType : Aspect.aspects.entrySet() )
		{
			// Get the aspect
			Aspect aspect = aspectType.getValue();

			// Create and register
			GaseousEssentia.create( aspect );
		}

	}

	/**
	 * Get the aspect this gas is based off of.
	 * 
	 * @return
	 */
	@Override
	public Aspect getAspect()
	{
		return this.associatedAspect;
	}

	/**
	 * Gets the color of the gas.
	 */
	@Override
	public int getColor()
	{
		if( this.associatedAspect != null )
		{
			return this.associatedAspect.getColor();
		}

		return super.getColor();
	}

	/**
	 * Gets the fluid form of this gas.
	 */
	@Override
	public Fluid getFluid()
	{
		return this;
	}

	/**
	 * Gets the localized version of the gasses name.
	 * 
	 * @deprecated
	 */
	@Deprecated
	@Override
	public String getLocalizedName()
	{
		return this.getLocalizedName( null );
	}

	/**
	 * Gets the localized version of the gasses name.
	 */
	@Override
	public String getLocalizedName( final FluidStack stack )
	{
		return this.associatedAspect.getName() + " " + StatCollector.translateToLocal( "thaumicenergistics.fluid.gaseous" );
	}

}
