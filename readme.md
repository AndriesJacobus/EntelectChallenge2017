<h2>First iteration of the Bot: GridBot</h2>

<ol>
  <li>
    Overall strategy
    <p>
      The general strategy is to fire in a grid type of formation to emulate the c# reference bot but with a slight deviation.
    </p>
  </li>
  
  <li>
    Placement:
    <p>
      The placement of the ships is hard-coded according to heat maps.<br/>
      These heat maps were generated by analysing the placement frequencies of the<br/>
      c# bot placements (which were random). The ships are placed in the areas with the<br/>
      smallest frequency according to the heat map for the specific map size.
    </p>
  </li>
  
  <li>
    Firing:<br/>
      The bot fires in a grid-like fashion, starting from the first column and<br/>
      moving upwards through all the rows of the column (x=0, y=0) towards (x=0, y={gridsize}).<br/>
      The bot skips every second row. The firing thus looks like (4x4 grid for example):<br/>
      
      [~][~][~][~]    to    [~][~][~][~]    to    [~][~][~][~]    to    [~][~][~][~]    to    [!][~][~][~]
      [~][~][~][~]    to    [~][~][~][~]    to    [~][~][~][~]    to    [!][~][~][~]    to    [!][~][~][~]
      [~][~][~][~]    to    [~][~][~][~]    to    [!][~][~][~]    to    [!][~][~][~]    to    [!][~][~][~]
      [~][~][~][~]    to    [!][~][~][~]    to    [!][~][~][~]    to    [!][~][~][~]    to    [!][~][~][~]
      
      [!][~][~][~]    to    [!][~][~][~]    to    [!][~][~][~]    to    [!][~][~][~]    to    [!][~][!][~]
      [!][~][~][~]    to    [!][~][~][~]    to    [!][~][~][~]    to    [!][~][!][~]    to    [!][~][!][~]
      [!][~][~][~]    to    [!][~][~][~]    to    [!][~][!][~]    to    [!][~][!][~]    to    [!][~][!][~]
      [!][~][~][~]    to    [!][~][!][~]    to    [!][~][!][~]    to    [!][~][!][~]    to    [!][~][!][~]
   
      Keys: ~ Water
            ! Shot
  </li>
  
  <li>
    Limitations:
    This bot is an extremely hackey solution to a close deadline - its strategy is far from perfect.<br/>
    
    * Hardcoded strategy: The solution isn't adaptive. It goes along the grid whether or not it hits
      a boat. The ideal is that this grid strategy whould serve as a (bad) way of probing the map to
      find a boat and then try to desproy that ship and continue probing until all the boats are destroyed.
      This method doesn't do this. I was in a hurry, okay!
      
    * Ineffective: The strategy is more effective than the grid solution of the c# reference bot, bit it's
      still ineffective agains any bot that can better probe the map and then destroy a ship when it finds it.
  </li>
</ol>