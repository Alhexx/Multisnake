package multisnake;

import java.util.Map;
import java.util.HashMap;
import java.util.List;
import java.util.ArrayList;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import java.util.Optional;

public class GameState {

	private final int rows;
	private final int cols;
	private final boolean teleporting;
	private final Map<Integer, Snake> snakes;
	private final List<Food> foods;
	//private final List<Obstacle> obstacles;
	
	public GameState() {
		this.rows = 20;
		this.cols = 20;
		this.teleporting = true;
		this.snakes = new HashMap<>();
		this.foods = new ArrayList<>();
		for (int i = 0; i < 10; i++) spawnFood();
	}

	public void tick() {
		snakes.forEach((k, v) -> {
			Pos nextPos = wrapPos(v.facing().normalVector().add(v.head()));
			Optional<Food> nextFood = foods.stream()
				.filter(f -> f.pos().equals(nextPos))
				.findAny();
			if (nextFood.isPresent()) {
				v.grow(nextPos);
				foods.remove(nextFood.get());
				spawnFood();
			} else {
				List<Pos> heads = snakes.values().stream()
					.map(s -> s.head())
					.collect(Collectors.toList());
				List<Pos> tails = snakes.values().stream()
					.map(s -> s.tail())
					.flatMap(l -> l.stream())
					.collect(Collectors.toList());
				Optional<Pos> obstacle = Stream.concat(
					heads.stream(), tails.stream()
				).filter(p -> p.equals(nextPos)).findAny();
				if (obstacle.isPresent()) {
					v.kill(randomFreePos());
				} else {
					v.step(nextPos);
				}
			}
		});
	}

	public int getRows() { return rows; }
	public int getCols() { return cols; }

	public int addSnake() {
		Snake snake = new Snake(randomFreePos());
		snakes.put(snakes.size()+1, snake);
		return snakes.size();
	}

	public void removeSnake(int id) {
		snakes.remove(id);
	}

	public Pos getSnakeHead(int id) {
		return snakes.get(id).head();
	}

	public List<Pos> getSnakeTail(int id) {
		return snakes.get(id).tail();
	}

	public void redirectSnake(int id, Direction direction) {
		snakes.get(id).face(direction);
	}

	public List<Pos> getFoods() {
		return foods.stream().map(f -> f.pos()).collect(Collectors.toList());
	}

	public void spawnFood() {
		foods.add(new Food(randomFreePos()));
	}

	private Pos randomPos() {
		return Pos.of(
			(int)(Math.random() * rows),
			(int)(Math.random() * cols)
		);
	}

	private Pos randomFreePos() {
		List<Pos> fo = foods.stream()
			.map(f -> f.pos())
			.collect(Collectors.toList());
		List<Pos> sh = snakes.values().stream()
			.map(s -> s.head())
			.collect(Collectors.toList());
		List<Pos> st = snakes.values().stream()
			.map(s -> s.tail())
			.flatMap(l -> l.stream())
			.collect(Collectors.toList());
		List<Pos> free = new ArrayList<>();
		for (int y = 0; y < rows; y++)
			for (int x = 0; x < cols; x++)
				free.add(Pos.of(x, y));
		free.removeAll(fo);
		free.removeAll(sh);
		free.removeAll(st);
		return free.get((int)(Math.random() * free.size()));
	}

	private Pos wrapPos(Pos pos) {
		if (!teleporting) return pos;
		if (pos.x < 0) pos = Pos.of(cols-1, pos.y);
		if (pos.y < 0) pos = Pos.of(pos.x, rows-1);
		if (pos.x > cols-1) pos = Pos.of(0, pos.y);
		if (pos.y > rows-1) pos = Pos.of(pos.x, 0);
		return pos;
	}

}
