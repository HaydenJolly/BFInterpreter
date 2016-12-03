/**
 * Add documentation eventually
 * Doesn't currently include functionality for the , instruction
 * Haven't tested if it properly deals with comments
 */
public class Interpreter
{
    private String instructions; 
    private char instruction;
    private byte [] mem;
    private int memIndex, instructionIndex, instructionEnd;

    public Interpreter(String _instructions)
    {
        instructions = _instructions;
        mem = new byte [30000];
        memIndex = 0;
        instructionIndex = 0;
        instructionEnd = instructions.length();
    }

    public void runProgram()
    {
        while(instructionIndex < instructionEnd)
        {
            instruction = instructions.charAt(instructionIndex);
            switch (instruction)
            {
                case '+':
                mem[memIndex] = (byte)(++mem[memIndex] % 256);
                break;

                case '-':
                if (mem[memIndex] > 0)
                {
                    mem[memIndex]--;
                }else
                {
                    mem[memIndex] = (byte)255;
                }
                break;

                case '>':
                memIndex = ++memIndex % 30000;
                break;

                case '<':
                if (memIndex > 0)
                {
                    memIndex--;
                }else
                {
                    memIndex = 29999;
                }
                break;

                case '[':
                if (mem[memIndex] == 0)
                {
                    instructionIndex = findLoopClose();
                }
                break;

                case ']':
                instructionIndex = findLoopOpen();
                break;

                case '.':
                System.out.print((char)mem[memIndex]);
                break;
            }
            instructionIndex++;
        }
    }

    public int findLoopClose()
    {
        int loopOpens = 0;
        boolean foundEnd = false;
        char localInstruction;
        int localInstructionIndex = instructionIndex;
        while (!foundEnd)
        {
            localInstruction = instructions.charAt(localInstructionIndex);
            if (localInstruction == '[')
            {
                loopOpens++;
            }
            else if (localInstruction == ']')
            {
                loopOpens--;
                if (loopOpens == 0)
                {
                    foundEnd = true;
                }
            }
            localInstructionIndex++;
        }
        return --localInstructionIndex;
    }

    public int findLoopOpen()//int instructionIndex)
    {
        int loopEnds = 0;
        boolean foundOpen = false;
        char localInstruction;
        int localInstructionIndex = instructionIndex;
        while (!foundOpen)
        {
            localInstruction = instructions.charAt(localInstructionIndex);
            if (localInstruction == ']')
            {
                loopEnds++;
            }
            if (localInstruction == '[')
            {
                loopEnds--;
            }
            if (loopEnds == 0 && localInstruction == '[')
            {
                foundOpen = true;
            }
            localInstructionIndex--;
        }
        return localInstructionIndex;
    }
}
