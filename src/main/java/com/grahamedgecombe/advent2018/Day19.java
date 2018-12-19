package com.grahamedgecombe.advent2018;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public final class Day19 {
	private static final Pattern IP_PATTERN = Pattern.compile("^#ip ([0-9]+)$");

	public static void main(String[] args) throws IOException {
		Program program = Program.parse(AdventUtils.readLines("day19.txt"));
		System.out.println(program.evaluate());
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
		ADDR(Day19::add, Day19::reg, Day19::reg),
		ADDI(Day19::add, Day19::reg, Day19::imm),
		MULR(Day19::mul, Day19::reg, Day19::reg),
		MULI(Day19::mul, Day19::reg, Day19::imm),
		BANR(Day19::ban, Day19::reg, Day19::reg),
		BANI(Day19::ban, Day19::reg, Day19::imm),
		BORR(Day19::bor, Day19::reg, Day19::reg),
		BORI(Day19::bor, Day19::reg, Day19::imm),
		SETR(Day19::set, Day19::reg, Day19::imm),
		SETI(Day19::set, Day19::imm, Day19::imm),
		GTIR(Day19::gt, Day19::imm, Day19::reg),
		GTRI(Day19::gt, Day19::reg, Day19::imm),
		GTRR(Day19::gt, Day19::reg, Day19::reg),
		EQIR(Day19::eq, Day19::imm, Day19::reg),
		EQRI(Day19::eq, Day19::reg, Day19::imm),
		EQRR(Day19::eq, Day19::reg, Day19::reg);

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

		public int evaluate() {
			int[] regs = new int[6];
			for (;;) {
				int index = regs[ip];
				if (index < 0 || index >= instructions.size()) {
					return regs[0];
				}

				Instruction insn = instructions.get(index);
				insn.opcode.evaluate(regs, insn.a, insn.b, insn.c);
				regs[ip]++;
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
