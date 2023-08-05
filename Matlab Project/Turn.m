function [B] = Turn(A)
    [rows,cols]=size(A);
    B=zeros(rows,cols);
    
    for i=1:rows
        for j=1:cols
            
            neighbors=0;
            alive=A(i,j);
            
            if j>1
                if A(i,j-1)==1
                    neighbors=neighbors+1;
                end
            end
            if j<cols
                if A(i,j+1)==1
                    neighbors=neighbors+1;
                end
            end
            
            if i>1
                if A(i-1,j)==1
                        neighbors=neighbors+1;
                end
                
                if j>1
                    if A(i-1,j-1)==1
                        neighbors=neighbors+1;
                    end
                end
                if j<cols
                    if A(i-1,j+1)==1
                        neighbors=neighbors+1;
                    end
                end
            end
            if i<rows
                if A(i+1,j)==1
                        neighbors=neighbors+1;
                end
                
                if j>1
                    if A(i+1,j-1)==1
                        neighbors=neighbors+1;
                    end
                end
                if j<cols
                    if A(i+1,j+1)==1
                        neighbors=neighbors+1;
                    end
                end
            end
            
            B(i,j)=State(alive,neighbors);
        end
    end
end
