package com.grahamedgecombe.advent2018;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public final class Day16 {
	private static final Pattern REGS_PATTERN = Pattern.compile("^(?:Before|After): +\\[([0-9]+), ([0-9]+), ([0-9]+), ([0-9]+)]$");

	public static void main(String[] args) throws IOException {
		String[] parts = AdventUtils.readString("day16.txt").split("\n\n\n\n");
		List<Sample> samples = Sample.parse(parts[0]);
		System.out.println(countMatchingSamples(samples));
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
		ADDR(Day16::add, Day16::reg, Day16::reg),
		ADDI(Day16::add, Day16::reg, Day16::imm),
		MULR(Day16::mul, Day16::reg, Day16::reg),
		MULI(Day16::mul, Day16::reg, Day16::imm),
		BANR(Day16::ban, Day16::reg, Day16::reg),
		BANI(Day16::ban, Day16::reg, Day16::imm),
		BORR(Day16::bor, Day16::reg, Day16::reg),
		BORI(Day16::bor, Day16::reg, Day16::imm),
		SETR(Day16::set, Day16::reg, Day16::imm),
		SETI(Day16::set, Day16::imm, Day16::imm),
		GTIR(Day16::gt, Day16::imm, Day16::reg),
		GTRI(Day16::gt, Day16::reg, Day16::imm),
		GTRR(Day16::gt, Day16::reg, Day16::reg),
		EQIR(Day16::eq, Day16::imm, Day16::reg),
		EQRI(Day16::eq, Day16::reg, Day16::imm),
		EQRR(Day16::eq, Day16::reg, Day16::reg);

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

	private static final class Instruction {
		public static Instruction parse(String line) {
			int[] ints = Arrays.stream(line.split(" ")).mapToInt(Integer::parseInt).toArray();
			return new Instruction(ints[0], ints[1], ints[2], ints[3]);
		}

		private final int opcode, a, b, c;

		public Instruction(int opcode, int a, int b, int c) {
			this.opcode = opcode;
			this.a = a;
			this.b = b;
			this.c = c;
		}
	}

	public static final class Sample {
		public static List<Sample> parse(String in) {
			List<Sample> samples = new ArrayList<>();

			for (String s : in.split("\n\n")) {
				String[] lines = s.split("\n");

				int[] before = parseRegs(lines[0]);
				Instruction instruction = Instruction.parse(lines[1]);
				int[] after = parseRegs(lines[2]);

				samples.add(new Sample(before, instruction, after));
			}

			return samples;
		}

		private static int[] parseRegs(String s) {
			Matcher m = REGS_PATTERN.matcher(s);
			if (!m.matches()) {
				throw new IllegalArgumentException();
			}
			return new int[] {
				Integer.parseInt(m.group(1)),
				Integer.parseInt(m.group(2)),
				Integer.parseInt(m.group(3)),
				Integer.parseInt(m.group(4))
			};
		}

		private final int[] before;
		private final Instruction instruction;
		private final int[] after;

		public Sample(int[] before, Instruction instruction, int[] after) {
			this.before = before;
			this.instruction = instruction;
			this.after = after;
		}
	}

	public static int countMatchingSamples(List<Sample> samples) {
		int count = 0;

		for (Sample sample : samples) {
			int matches = 0;

			for (Opcode opcode : Opcode.values()) {
				int[] regs = Arrays.copyOf(sample.before, sample.before.length);
				opcode.evaluate(regs, sample.instruction.a, sample.instruction.b, sample.instruction.c);
				if (Arrays.equals(regs, sample.after)) {
					matches++;
				}
			}

			if (matches >= 3) {
				count++;
			}
		}

		return count;
	}
}
