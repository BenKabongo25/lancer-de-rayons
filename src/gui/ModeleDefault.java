package gui;

public class ModeleDefault {

    public static String examplePOV = "#version 3.6;\n" +
            "\n" +
            "\n" +
            "#declare variableTest = 0.7;\n" +
            "#declare foobar = 1;\n" +
            "#declare troisieme_Variable = 0.9;\n" +
            "#declare Red = <1.0, 0, 0>;\n" +
            "#declare SteelBlue = <0.0, 0.0, 1.0>;\n" +
            "#declare blanc = <1, 1, 1>;\n" +
            "\n" +
            " background   { color rgb <variableTest, foobar, troisieme_Variable> }\n" +
            "                     \n" +
            "camera{ location  <10 ,15, 10>\n" +
            "        angle 90 // direction 2z \n" +
            "        right     x*image_width/image_width // keep propotions with any aspect ratio\n" +
            "        look_at   <0,2,00>\n" +
            "      }\n" +
            "sphere {<0, 3, 0>,2\n" +
            "   pigment { color <1, 1, 1, 0.9>}\n" +
            "   finish {phong .2 reflection 0}\n" +
            "   interior { ior 1.7 }\n" +
            "}\n" +
            "\n" +
            "sphere {<-24, 3, -5>,2\n" +
            "   pigment { color <1, 1, 1, 0>}\n" +
            "   finish {phong .2 reflection 0}\n" +
            "}\n" +
            "\n" +
            "sphere {<-50, 3, -50>,40\n" +
            "   pigment { color <1, 1, 1, 1>}\n" +
            "   finish {phong .2 reflection 1}\n" +
            "}\n" +
            "sphere {<15, 3, 14>,2\n" +
            "   pigment { color blanc }\n" +
            "   finish {phong .2 reflection 0}\n" +
            "}sphere {<6, 2, 4>,2\n" +
            "   pigment { color blanc }\n" +
            "}\n" +
            " box          { <-5, 5, -15>,\n" +
            "                <0.5, 0.5, -10>\n" +
            "                pigment { color blanc  }\n" +
            "               finish  { specular 0.6 }\n" +
            " }\n" +
            "\n" +
            "//Cette sphère va servir de sol\n" +
            "sphere {<0, -100003, 0>,100000\n" +
            "   pigment { color blanc }\n" +
            "   finish {phong .2 reflection 0}\n" +
            "}     \n" +
            "\n" +
            "\n" +
            "\n" +
            "light_source { <0, 0, -10>\n" +
            "                color rgb <1, 0, 0>\n" +
            "                fade_distance 3\n" +
            "                fade_power 1\n" +
            "}\n" +
            "light_source { <0, 10, 0>\n" +
            "                color rgb <0, 1, 0>\n" +
            "                fade_distance 3\n" +
            "                fade_power 1\n" +
            "}\n" +
            "light_source { <-20, 0, 0>\n" +
            "                color rgb <0, 0, 1>\n" +
            "                fade_distance 40\n" +
            "                fade_power 1\n" +
            "}\n"/*+



            " box          { <-0.5, -0.5, -0.5>,\n" +
            "                <0.5, 0.5, 0.5>\n" +
            "                texture { pigment { color Red }\n" +
            "                          finish  { specular 0.6 }\n" +
            "                        }\n" +
            "                scale <1,1,1>\n" +
            "                rotate <45,46,47> }\n\n" +

            "plane{ <0,1,0>, 0  \n" +
            "       texture{ Cork }  \n" +
            "     }\n\n" +



            "cylinder{ <0,0,0>,<0,1,0>, 0.25 \n" +
            "         pigment{color rgb<1,0.6,0>}\n" +
            "        }\n\n" +

            "cone{ <0,0,0>, 1, <0,1.75,0>, 0.5 \n" +
            "      pigment{color rgb<0.4,0.7,0>}\n" +
            "      finish {phong 1}\n" +
            "    }\n\n"*/;


}
