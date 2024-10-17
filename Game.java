import java.util.ArrayList;
import java.util.Random;
import java.util.Scanner;

// Base Character Class
abstract class Character {
    private String name;
    private int level;
    private int hp;
    private int maxHp;
    private int mp;
    private int maxMp;
    private int baseAttack;
    private ArrayList<Item> inventory;

    public Character(String name, int level, int hp, int mp, int baseAttack) {
        this.name = name;
        this.level = level;
        this.hp = hp;
        this.maxHp = hp;
        this.mp = mp;
        this.maxMp = mp;
        this.baseAttack = baseAttack;
        this.inventory = new ArrayList<>();
    }

    public String getName() {
        return name;
    }

    public int getLevel() {
        return level;
    }

    public int getHp() {
        return hp;
    }

    public int getMp() {
        return mp;
    }

    public void setHp(int hp) {
        this.hp = Math.min(hp, maxHp);
    }

    public void setMp(int mp) {
        this.mp = Math.min(mp, maxMp);
    }

    public int getBaseAttack() {
        return baseAttack;
    }

    public void levelUp() {
        level++;
        maxHp += 30;
        maxMp += 10;
        hp = maxHp;
        mp = maxMp;
        baseAttack += 5;
        System.out.println(name + " leveled up to level " + level + "!");
        System.out.println("HP: " + hp + ", MP: " + mp + ", Attack Power: " + baseAttack);
    }

    public void addItem(Item item) {
        inventory.add(item);
    }

    public ArrayList<Item> getInventory() {
        return inventory;
    }

    public abstract void attack(Enemy enemy);

    public abstract void useSkill(Enemy enemy);

    public abstract int getSkillCost();
}

// Warrior Class
class Warrior extends Character {
    public Warrior(String name) {
        super(name, 1, 100, 30, 15);
    }

    @Override
    public void attack(Enemy enemy) {
        int damage = getBaseAttack() + new Random().nextInt(10);
        System.out.println(getName() + " attacks with a sword for " + damage + " damage!");
        enemy.takeDamage(damage);
    }

    @Override
    public void useSkill(Enemy enemy) {
        int skillCost = getSkillCost();
        if (getMp() < skillCost) {
            System.out.println("Not enough MP to use Power Strike!");
            return;
        }
        setMp(getMp() - skillCost);
        int damage = getBaseAttack() * 2 + new Random().nextInt(10);
        System.out.println(getName() + " uses Power Strike for " + damage + " damage!");
        enemy.takeDamage(damage);
    }

    @Override
    public int getSkillCost() {
        return 15;
    }
}

// Mage Class
class Mage extends Character {
    public Mage(String name) {
        super(name, 1, 80, 50, 20);
    }

    @Override
    public void attack(Enemy enemy) {
        int damage = getBaseAttack() + new Random().nextInt(30);  // Mage does baseAttack + random damage
        System.out.println(getName() + " casts a spell for " + damage + " damage!");
        enemy.takeDamage(damage);
    }

    @Override
    public void useSkill(Enemy enemy) {
        int skillCost = getSkillCost();
        if (getMp() < skillCost) {
            System.out.println("Not enough MP to use Fireball!");
            return;
        }
        setMp(getMp() - skillCost);
        int damage = getBaseAttack() + new Random().nextInt(50);  // Fireball deals 2x base attack + more random damage
        System.out.println(getName() + " casts Fireball for " + damage + " damage!");
        enemy.takeDamage(damage);
    }

    @Override
    public int getSkillCost() {
        return 20;
    }
}

// Archer Class
class Archer extends Character {
    public Archer(String name) {
        super(name, 1, 90, 40, 18);
    }

    @Override
    public void attack(Enemy enemy) {
        int damage = getBaseAttack() + new Random().nextInt(10);
        System.out.println(getName() + " shoots an arrow for " + damage + " damage!");
        enemy.takeDamage(damage);
    }

    @Override
    public void useSkill(Enemy enemy) {
        int skillCost = getSkillCost();
        if (getMp() < skillCost) {
            System.out.println("Not enough MP to use Piercing Shot!");
            return;
        }
        setMp(getMp() - skillCost);
        int damage = getBaseAttack() + new Random().nextInt(10);
        System.out.println(getName() + " uses Piercing Shot for " + damage + " damage, the enemy's defense has gone down!");
        enemy.takeDamage(damage);
        enemy.weakenDefense();
    }

    @Override
    public int getSkillCost() {
        return 18;
    }
}

// Enemy Class
class Enemy {
    private String type;
    private int hp;
    private int maxHp;
    private int difficulty;
    private boolean weakened;

    public Enemy(String type, int difficulty) {
        this.type = type;
        this.difficulty = difficulty;
        this.maxHp = 50 * difficulty;
        this.hp = maxHp;
        this.weakened = false;
    }

    public String getType() {
        return type;
    }

    public int getHp() {
        return hp;
    }

    public void takeDamage(int damage) {
        hp -= damage;
        System.out.println(type + " takes " + damage + " damage. HP left: " + hp);
    }

    public boolean isDefeated() {
        return hp <= 0;
    }

    public void weakenDefense() {
        weakened = true;
        System.out.println(type + "'s defenses have been weakened!");
    }

    public int attackPlayer(ArrayList<Character> party) {
        int damage = (weakened ? difficulty * 5 : difficulty * (10 + new Random().nextInt(5)));
        for (Character character : party) {
            if (character.getHp() > 0) {
                System.out.println(type + " attacks " + character.getName() + " for " + damage + " damage!");
                character.setHp(character.getHp() - damage);
            }
        }
        return damage;
    }

    public Item dropItem() {
        String[] items = {"Health Potion", "MP Potion"};
        String droppedItem = items[new Random().nextInt(items.length)];
        if (droppedItem.equals("Health Potion")) {
            return new Item("Health Potion", "Health", "Restores 50 HP");
        } else {
            return new Item("MP Potion", "Mana", "Restores 20 MP");
        }
    }

    public void weakenDifficulty() {
        if (difficulty > 1) {
            difficulty--;
            maxHp = 50 * difficulty;
            hp = maxHp;
            System.out.println(type + " is slightly weakened. New HP: " + hp);
        }
    }
}

// Item Class
class Item {
    private String name;
    private String type;
    private String effect;

    public Item(String name, String type, String effect) {
        this.name = name;
        this.type = type;
        this.effect = effect;
    }

    public String getName() {
        return name;
    }

    public String getEffect() {
        return effect;
    }

    public void use(Character character) {
        System.out.println(character.getName() + " uses " + name + " and gains the effect: " + effect);
        if (type.equals("Health")) {
            character.setHp(character.getHp() + 50);
        } else if (type.equals("Mana")) {
            character.setMp(character.getMp() + 20);
        }
    }
}

public class Game {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        ArrayList<Character> party = new ArrayList<>();

        System.out.println("Choose your first character:");
        System.out.println("1. Warrior");
        System.out.println("2. Mage");
        System.out.println("3. Archer");
        int firstChoice = scanner.nextInt();
        switch (firstChoice) {
            case 1:
                party.add(new Warrior("Warrior"));
                break;
            case 2:
                party.add(new Mage("Mage"));
                break;
            case 3:
                party.add(new Archer("Archer"));
                break;
            default:
                System.out.println("Invalid choice, defaulting to Warrior.");
                party.add(new Warrior("Warrior"));
                break;
        }

        System.out.println("Choose your second character:");
        System.out.println("1. Warrior");
        System.out.println("2. Mage");
        System.out.println("3. Archer");
        int secondChoice = scanner.nextInt();
        switch (secondChoice) {
            case 1:
                party.add(new Warrior("Warrior 2"));
                break;
            case 2:
                party.add(new Mage("Mage 2"));
                break;
            case 3:
                party.add(new Archer("Archer 2"));
                break;
            default:
                System.out.println("Invalid choice, defaulting to Warrior 2.");
                party.add(new Warrior("Warrior 2"));
                break;
        }

        int enemiesDefeated = 0;

        while (party.stream().anyMatch(character -> character.getHp() > 0)) {
            Enemy enemy = new Enemy("Goblin", enemiesDefeated / 2 + 1);
            System.out.println("A wild " + enemy.getType() + " appears! HP: " + enemy.getHp());

            while (!enemy.isDefeated() && party.stream().anyMatch(character -> character.getHp() > 0)) {
                for (Character player : party) {
                    if (player.getHp() > 0) {
                        System.out.println("Your HP: " + player.getHp() + ", MP: " + player.getMp());
                        System.out.println("Enemy HP: " + enemy.getHp());
                        System.out.println("1. Attack");
                        if (player.getLevel() >= 2) {
                            System.out.println("2. Use Skill");
                        }
                        System.out.println("3. Use Item");
                        int action = scanner.nextInt();

                        if (action == 1) {
                            player.attack(enemy);
                        } else if (action == 2 && player.getLevel() >= 2) {
                            player.useSkill(enemy);
                        } else if (action == 3) {
                            if (player.getInventory().isEmpty()) {
                                System.out.println("You have no items!");
                            } else {
                                System.out.println("Select an item:");
                                for (int i = 0; i < player.getInventory().size(); i++) {
                                    System.out.println((i + 1) + ". " + player.getInventory().get(i).getName());
                                }
                                int itemChoice = scanner.nextInt();
                                if (itemChoice > 0 && itemChoice <= player.getInventory().size()) {
                                    player.getInventory().get(itemChoice - 1).use(player);
                                    player.getInventory().remove(itemChoice - 1);
                                } else {
                                    System.out.println("Invalid item choice.");
                                }
                            }
                        } else {
                            System.out.println("Invalid action.");
                        }

                        if (enemy.getHp() > 0) {
                            enemy.attackPlayer(party);
                        }
                    }
                }
            }

            if (party.stream().anyMatch(character -> character.getHp() > 0)) {
                System.out.println("You defeated the " + enemy.getType() + "!");
                enemiesDefeated++;
                Item droppedItem = enemy.dropItem();
                System.out.println("The enemy dropped a " + droppedItem.getName() + "!");
                for (Character character : party) {
                    character.addItem(droppedItem);
                }

                if (enemiesDefeated % 2 == 0) {
                    for (Character character : party) {
                        character.levelUp();
                    }
                } else {
                    enemy.weakenDifficulty();
                }
            } else {
                System.out.println("All characters have been defeated.");
            }
        }
    }
}
