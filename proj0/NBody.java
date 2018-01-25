public class NBody
{
  public static double readRadius(String filename)
  {
    In in = new In(filename);
    int buffer = in.readInt();
    return in.readDouble();
  }

  public static Planet[] readPlanets(String filename)
  {
    In in = new In(filename);
    Planet[] planets = new Planet[in.readInt()];
    double buffer = in.readDouble();
    for (int i = 0; i < planets.length; i++)
    {
      planets[i] = new Planet(in.readDouble(), in.readDouble(),
      in.readDouble(), in.readDouble(), in.readDouble(), in.readString());
    }
    return planets;
  }

  public static void main(String[] args)
  {
    StdAudio.loop("audio/2001.mid");

    double T = Double.parseDouble(args[0]);
    double dt = Double.parseDouble(args[1]);
    String filename = args[2];
    double radius = readRadius(filename);
    Planet[] planets = readPlanets(filename);

    StdDraw.setScale(-radius, radius);
    StdDraw.picture(0, 0, "images/starfield.jpg");
    for (Planet p : planets)
    {
      p.draw();
    }

    StdDraw.enableDoubleBuffering();
    for (int time = 0; time < T; time += dt)
    {
      Double[] xForces = new Double[planets.length];
      Double[] yForces = new Double[planets.length];
      for (int i = 0; i < planets.length; i++)
      {
        xForces[i] = planets[i].calcNetForceExertedByX(planets);
        yForces[i] = planets[i].calcNetForceExertedByY(planets);
      }
      for (int i = 0; i < planets.length; i++)
      {
        planets[i].update(dt, xForces[i], yForces[i]);
      }
      StdDraw.picture(0, 0, "images/starfield.jpg");
      for (Planet p : planets)
      {
        p.draw();
      }
      StdDraw.show();
      StdDraw.pause(10);
    }

    System.out.println(planets.length);
    System.out.println(radius);
    for (Planet p : planets)
    {
      StdOut.printf("%11.4e %11.4e %11.4e %11.4e %11.4e %12s\n",
      p.xxPos, p.yyPos, p.xxVel, p.yyVel, p.mass, p.imgFileName);
    }
/*
    StdOut.printf(planets.length);
    StdOut.printf(radius);
    for (Planet p : planets)
    {
      StdOut.printf(p.xxPos, p.yyPos, p.xxVel, p.yyVel, p.mass, p.imgFileName);
    }
*/
  }
}
