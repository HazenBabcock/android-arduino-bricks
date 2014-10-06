$fn = 100;

// 1/2 length pin
module half_pin(bottom_rim_height = 0.8,
					 top_rim_height = 0.8)
{
	translate([0,0,0.8 - bottom_rim_height])
		cylinder(h=bottom_rim_height, r=3.3);
	cylinder(h=3.92, r=2.5);
	translate([0,0,3.12])
	cylinder(h=top_rim_height, r=3.3);
}

// 1 length pin
module pin(bottom_rim_height = 0.8,
			  top_rim_height = 0.8)
{
	translate([0,0,0.8 - bottom_rim_height])
		cylinder(h=bottom_rim_height, r=3.3);
	cylinder(h=7.84, r=2.5);
	translate([0,0,7.04])
		cylinder(h=top_rim_height, r=3.3);
}

half_pin();
translate([8,0,0])
pin();
