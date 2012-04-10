/*	
 
 
 * 
 
 *  documentation files (the "Software"), to deal in the Software without restriction, including without 
 *  limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of
 *	the Software, and to permit persons to whom the Software is furnished to do so, subject to the following 
 *	conditions:
 * 
 *	The above copyright notice and this permission notice shall be included in all copies or substantial 
 *	portions of the Software.
 *
 *	THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT 
 *	LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO 
 *	EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER 
 *	IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR 
 *	THE USE OR OTHER DEALINGS IN THE SOFTWARE. 
 */
package cn.edu.nju.software.util;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.TimeZone;

import cn.edu.nju.software.GuitarModule.GUITARConstants;

/**
 * Static utility functions which might be used globally
 * 
 * @author     </a>
 * 
 */
public class Util {

    /**
     * Hide the constructor
     */
    private Util() {
    }

    /**
     * Get a list of string from a text file. Used for ignoring widgets,
     * windows...
     * 
     * @param sFileName
     * @param isOrdered
     * @return
     */

    public static List<String> getListFromFile(String sFileName,
            boolean isOrdered) {

        List<String> retList;

        if (isOrdered) {
            retList = new LinkedList<String>();
        } else {
            retList = new ArrayList<String>();
        }

        try {
            
            ClassLoader cl = Util.class.getClassLoader();
            InputStream is = cl.getResourceAsStream(sFileName);
            InputStreamReader isr = new InputStreamReader(is);
            BufferedReader br = new BufferedReader(isr);
            
//            BufferedReader br = new BufferedReader(new FileReader(sFileName));

            String line;
            while ((line = br.readLine()) != null) {
                line = line.trim();
                if (!line.startsWith(GUITARConstants.IGNORE_COMMENT_PREFIX)
                        && !("".equals(line)))
                    retList.add(line);
            }
        } catch (FileNotFoundException e) {
			System.out.print(e);
        } catch (Exception e) {
            System.out.println( sFileName + " not found") ;
        }
        return retList;
    }

    @Deprecated
    public static void getCollectionFromFile(String sFileName,
            Collection<String> collection) {
        try {
            BufferedReader is = new BufferedReader(new FileReader(sFileName));

            String line;
            while ((line = is.readLine()) != null) {
                line = line.trim();
                if (!line.startsWith(GUITARConstants.IGNORE_COMMENT_PREFIX)
                        && !("".equals(line)))
                    collection.add(line);
            }
        } catch (FileNotFoundException e) {
            // TODO Auto-generated catch block
			System.out.print(e);
        } catch (IOException e) {
            // TODO Auto-generated catch block
			System.out.print(e);
        }
    }

    /**
     * get TimeStamp
     * 
     * @return
     */
    public static String getTimeStamp() {
        DateFormat df = new SimpleDateFormat("yyyyMMddhhmmss");
        df.setTimeZone(TimeZone.getTimeZone("GMT"));
        return df.format(new Date());
    }

}
