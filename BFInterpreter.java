public class BFInterpreter
{
    private String instructions; 
    private char instruction;
    private byte [] mem;
    private int memIndex, instructionIndex, instructionEnd;

    public BFInterpreter(String _instructions)
    {
        instructions = _instructions;
        mem = new byte [30000];
        memIndex = 0;
        instructionIndex = 0;
        instructionEnd = instructions.length();
    }

    public void runProgram()
    {
        //iterate over all instructions
        while(instructionIndex < instructionEnd)
        {
            instruction = instructions.charAt(instructionIndex);
            switch (instruction)
            {
                //increments memory at memIndex (wraps around to 0 after 256)
                case '+':
                mem[memIndex] = (byte)(++mem[memIndex] % 256);
                break;
                
                //decrement memory at memIndex (wraps to 255 below 0)
                case '-':
                if (mem[memIndex] > 0)
                {
                    mem[memIndex]--;
                }else
                {
                    mem[memIndex] = (byte)255;
                }
                break;
                
                //points memIndex one unit forward (wraps back to 0)
                case '>':
                memIndex = ++memIndex % 30000;
                break;

                //points memIndex one unit backwards (wraps to end)
                case '<':
                if (memIndex > 0)
                {
                    memIndex--;
                }else
                {
                    memIndex = 29999;
                }
                break;

                //checks loop conidition and seeks loop close
                case '[':
                if (mem[memIndex] == 0)
                {
                    instructionIndex = findLoopClose();
                }
                break;

                //seeks loop open
                case ']':
                instructionIndex = findLoopOpen();
                break;

                //outputs the ascii character associated with current memory value
                case '.':
                System.out.print((char)mem[memIndex]);
                break;
                    
                //sets the current memory location to the value of an ascii character input
                case ',':
                System.out.print("\nEnter an ASCII character whose value will be stored in the current cell: ");
                mem[memIndex] = (byte)in.next().charAt(0);
                in.nextLine();
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

    public int findLoopOpen()
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
