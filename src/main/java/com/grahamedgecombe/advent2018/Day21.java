package com.grahamedgecombe.advent2018;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public final class Day21 {
	private static final Pattern IP_PATTERN = Pattern.compile("^#ip ([0-9]+)$");

	public static void main(String[] args) throws IOException {
		Program program = Program.parse(AdventUtils.readLines("day21.txt"));
		System.out.println(program.getMinInstructionsR0());
		System.out.println(program.getMaxInstructionsR0());
	}

	private static int add(int a, int b) {
		return a + b;
	}

	private static int mul(int a, int b) {
		return a * b;
	}

	private static int ban(int a, int b) {
		return a & b;
	}

	private static int bor(int a, int b) {
		return a | b;
	}

	private static int set(int a, int b) {
		return a;
	}

	private static int gt(int a, int b) {
		return a > b ? 1 : 0;
	}

	private static int eq(int a, int b) {
		return a == b ? 1 : 0;
	}

	private static int imm(int[] regs, int operand) {
		return operand;
	}

	private static int reg(int[] regs, int operand) {
		return regs[operand];
	}

	private interface Func {
		int evaluate(int a, int b);
	}

	private interface Operand {
		int evaluate(int[] regs, int operand);
	}

	private enum Opcode {
		ADDR(Day21::add, Day21::reg, Day21::reg),
		ADDI(Day21::add, Day21::reg, Day21::imm),
		MULR(Day21::mul, Day21::reg, Day21::reg),
		MULI(Day21::mul, Day21::reg, Day21::imm),
		BANR(Day21::ban, Day21::reg, Day21::reg),
		BANI(Day21::ban, Day21::reg, Day21::imm),
		BORR(Day21::bor, Day21::reg, Day21::reg),
		BORI(Day21::bor, Day21::reg, Day21::imm),
		SETR(Day21::set, Day21::reg, Day21::imm),
		SETI(Day21::set, Day21::imm, Day21::imm),
		GTIR(Day21::gt, Day21::imm, Day21::reg),
		GTRI(Day21::gt, Day21::reg, Day21::imm),
		GTRR(Day21::gt, Day21::reg, Day21::reg),
		EQIR(Day21::eq, Day21::imm, Day21::reg),
		EQRI(Day21::eq, Day21::reg, Day21::imm),
		EQRR(Day21::eq, Day21::reg, Day21::reg);

		private final Func func;
		private final Operand a, b;

		Opcode(Func func, Operand a, Operand b) {
			this.func = func;
			this.a = a;
			this.b = b;
		}

		public void evaluate(int[] regs, int a, int b, int c) {
			regs[c] = func.evaluate(this.a.evaluate(regs, a), this.b.evaluate(regs, b));
		}
	}

	public static final class Program {
		public static Program parse(List<String> lines) {
			int ip = 0;
			List<Instruction> instructions = new ArrayList<>();

			for (String line : lines) {
				Matcher m = IP_PATTERN.matcher(line);
				if (m.matches()) {
					ip = Integer.parseInt(m.group(1));
				} else {
					instructions.add(Instruction.parse(line));
				}
			}

			return new Program(ip, instructions);
		}

		private final int ip;
		private final List<Instruction> instructions;

		public Program(int ip, List<Instruction> instructions) {
			this.ip = ip;
			this.instructions = instructions;
		}

		public int getMinInstructionsR0() {
			return getR0(true);
		}

		public int getMaxInstructionsR0() {
			return getR0(false);
		}

		private int getR0(boolean part1) {
			Set<Integer> seen = new HashSet<>();
			int lastSeen = 0;

			int[] regs = new int[6];
			for (;;) {
				int index = regs[ip];
				if (index < 0 || index >= instructions.size()) {
					return regs[0];
				}

				Instruction insn = instructions.get(index);
				insn.opcode.evaluate(regs, insn.a, insn.b, insn.c);
				regs[ip]++;

				if (regs[ip] == 17) {
					regs[2] = ((regs[3] - 256) / 256) + 1;
					regs[ip] = 26;
				} else if (regs[ip] == 28) {
					if (part1) {
						return regs[5];
					} else if (!seen.add(regs[5])) {
						return lastSeen;
					}
					lastSeen = regs[5];
				}
			}
		}
	}

	private static final class Instruction {
		public static Instruction parse(String line) {
			String[] parts = line.split(" ");
			Opcode opcode = Opcode.valueOf(parts[0].toUpperCase());
			int a = Integer.parseInt(parts[1]);
			int b = Integer.parseInt(parts[2]);
			int c = Integer.parseInt(parts[3]);
			return new Instruction(opcode, a, b, c);
		}

		private final Opcode opcode;
		private final int a, b, c;

		public Instruction(Opcode opcode, int a, int b, int c) {
			this.opcode = opcode;
			this.a = a;
			this.b = b;
			this.c = c;
		}
	}
}
