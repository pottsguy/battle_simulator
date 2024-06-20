SELECT class, Count(class) FROM pregens WHERE strength = dexterity     AND strength     > 12 AND intelligence < strength     AND charisma     < strength GROUP BY class ORDER BY class;
SELECT class, Count(class) FROM pregens WHERE strength = intelligence  AND strength     > 12 AND dexterity    < strength     AND charisma     < strength GROUP BY class ORDER BY class;
SELECT class, Count(class) FROM pregens WHERE strength = charisma      AND strength     > 12 AND dexterity    < strength     AND intelligence < strength GROUP BY class ORDER BY class; 
SELECT class, Count(class) FROM pregens WHERE dexterity = intelligence AND dexterity    > 12 AND strength     < dexterity    AND charisma     < dexterity GROUP BY class ORDER BY class; 
SELECT class, Count(class) FROM pregens WHERE dexterity = charisma     AND dexterity    > 12 AND strength     < dexterity    AND intelligence < dexterity GROUP BY class ORDER BY class; 
SELECT class, Count(class) FROM pregens WHERE intelligence = charisma  AND intelligence > 12 AND strength     < intelligence AND dexterity    < intelligence GROUP BY class ORDER BY class; 
