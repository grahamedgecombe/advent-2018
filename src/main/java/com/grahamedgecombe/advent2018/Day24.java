package com.grahamedgecombe.advent2018;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.google.common.base.MoreObjects;
import com.google.common.collect.Streams;

public final class Day24 {
	private static final Pattern GROUP_PATTERN = Pattern.compile("^([0-9]+) units each with ([0-9]+) hit points " +
		"(\\(.*\\) )?with an attack that does ([0-9]+) ([^ ]+) damage at initiative ([0-9]+)$");

	public static void main(String[] args) throws IOException {
		Simulator simulator = Simulator.parse(AdventUtils.readLines("day24.txt"));
		System.out.println(simulator.fight());
	}

	public static final class Simulator {
		public static Simulator parse(List<String> lines) {
			List<Group> immuneGroups = new ArrayList<>();
			List<Group> infectionGroups = new ArrayList<>();
			boolean immune = true;

			for (String line : lines) {
				if (line.equals("Immune System:")) {
					immune = true;
				} else if (line.equals("Infection:")) {
					immune = false;
				} else if (!line.isEmpty()) {
					Group group = Group.parse(line);
					if (immune) {
						immuneGroups.add(group);
					} else {
						infectionGroups.add(group);
					}
				}
			}

			return new Simulator(immuneGroups, infectionGroups);
		}

		private final List<Group> immuneGroups, infectionGroups;

		public Simulator(List<Group> immuneGroups, List<Group> infectionGroups) {
			this.immuneGroups = immuneGroups;
			this.infectionGroups = infectionGroups;
		}

		private void selectTargets(List<Group> attackers, List<Group> defenders) {
			attackers.forEach(attacker -> attacker.target = null);
			defenders.forEach(defender -> defender.targetedBy = null);

			attackers.stream()
				.filter(Group::isAlive)
				.sorted(Comparator.comparingInt(Group::getEffectivePower)
					.thenComparingInt(Group::getInitiative)
					.reversed())
				.forEachOrdered(attacker -> selectTarget(attacker, defenders));
		}

		private void selectTarget(Group attacker, List<Group> defenders) {
			defenders.stream()
				.filter(Group::isAlive)
				.filter(defender -> defender.targetedBy == null)
				.filter(defender -> getDamage(attacker, defender) != 0)
				.max(Comparator.comparingInt((Group defender) -> getDamage(attacker, defender))
					.thenComparingInt(Group::getEffectivePower)
					.thenComparingInt(Group::getInitiative)
				)
				.ifPresent(defender -> {
					defender.targetedBy = attacker;
					attacker.target = defender;
				});
		}

		private int getDamage(Group attacker, Group defender) {
			if (defender.immuneAttackTypes.contains(attacker.attackType)) {
				return 0;
			} else if (defender.weakAttackTypes.contains(attacker.attackType)) {
				return attacker.getEffectivePower() * 2;
			} else {
				return attacker.getEffectivePower();
			}
		}

		private void attack() {
			Streams.concat(immuneGroups.stream(), infectionGroups.stream())
				.filter(group -> group.target != null)
				.sorted(Comparator.comparingInt(Group::getInitiative).reversed())
				.forEachOrdered(group -> {
					if (group.isAlive()) {
						group.target.hit(getDamage(group, group.target));
					}
				});
		}

		public int fight() {
			for (;;) {
				selectTargets(immuneGroups, infectionGroups);
				selectTargets(infectionGroups, immuneGroups);
				attack();

				if (immuneGroups.stream().noneMatch(Group::isAlive)) {
					return infectionGroups.stream().mapToInt(Group::getUnits).sum();
				} else if (infectionGroups.stream().noneMatch(Group::isAlive)) {
					return immuneGroups.stream().mapToInt(Group::getUnits).sum();
				}
			}
		}
	}

	private static final class Group {
		public static Group parse(String line) {
			Matcher matcher = GROUP_PATTERN.matcher(line);
			if (!matcher.matches()) {
				throw new IllegalArgumentException();
			}

			int units = Integer.parseInt(matcher.group(1));
			int hitPoints = Integer.parseInt(matcher.group(2));
			int attackDamage = Integer.parseInt(matcher.group(4));
			String attackType = matcher.group(5);
			int initiative = Integer.parseInt(matcher.group(6));

			Set<String> immuneAttackTypes = new HashSet<>();
			Set<String> weakAttackTypes = new HashSet<>();

			String immuneWeakLists = matcher.group(3);
			if (immuneWeakLists != null) {
				immuneWeakLists = immuneWeakLists.replaceAll("^\\(", "").replaceAll("\\) $", "");
				for (String list : immuneWeakLists.split("; ")) {
					if (list.startsWith("immune to ")) {
						list = list.replaceFirst("^immune to ", "");
						immuneAttackTypes.addAll(Arrays.asList(list.split(", ")));
					} else if (list.startsWith("weak to ")) {
						list = list.replaceFirst("^weak to ", "");
						weakAttackTypes.addAll(Arrays.asList(list.split(", ")));
					} else {
						throw new IllegalArgumentException();
					}
				}
			}

			return new Group(units, hitPoints, immuneAttackTypes, weakAttackTypes, attackDamage, attackType, initiative);
		}

		private int units;
		private final int hitPoints;
		private final Set<String> immuneAttackTypes, weakAttackTypes;
		private final int attackDamage;
		private final String attackType;
		private final int initiative;
		private Group targetedBy, target;

		public Group(int units, int hitPoints, Set<String> immuneAttackTypes, Set<String> weakAttackTypes, int attackDamage, String attackType, int initiative) {
			this.units = units;
			this.hitPoints = hitPoints;
			this.immuneAttackTypes = immuneAttackTypes;
			this.weakAttackTypes = weakAttackTypes;
			this.attackDamage = attackDamage;
			this.attackType = attackType;
			this.initiative = initiative;
		}

		public int getUnits() {
			return units;
		}

		public int getEffectivePower() {
			return units * attackDamage;
		}

		public int getInitiative() {
			return initiative;
		}

		public boolean isAlive() {
			return units > 0;
		}

		public void hit(int damage) {
			units = Math.max(units - (damage / hitPoints), 0);
		}

		@Override
		public String toString() {
			return MoreObjects.toStringHelper(this)
				.add("units", units)
				.add("hitPoints", hitPoints)
				.add("immuneAttackTypes", immuneAttackTypes)
				.add("weakAttackTypes", weakAttackTypes)
				.add("attackDamage", attackDamage)
				.add("attackType", attackType)
				.add("initiative", initiative)
				.add("targetedBy", targetedBy)
				.add("target", target)
				.toString();
		}
	}
}
